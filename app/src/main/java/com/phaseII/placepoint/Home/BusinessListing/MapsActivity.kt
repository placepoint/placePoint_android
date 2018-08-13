package com.phaseII.placepoint.Home.BusinessListing

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.phaseII.placepoint.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    internal lateinit var MarkerPoints: ArrayList<LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        MarkerPoints = ArrayList<LatLng>()

    }

    private fun getUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude


        // Sensor enabled
        val sensor = "sensor=false"

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor"

        // Output format
        val output = "json"

        // Building the url to the web service
        val url = "https://maps.googleapis.com/maps/api/directions/$output?$parameters"


        return url
    }


    // Fetches data from url passed
    private inner class FetchUrl : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg url: String): String {

            // For storing data from web service
            var data = ""

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0])
                Log.d("Background Task data", data.toString())
            } catch (e: Exception) {
                Log.d("Background Task", e.toString())
            }

            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            val parserTask = ParserTask()

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result)

        }
    }


    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection

            // Connecting to url
            urlConnection.connect()

            // Reading data from url
            iStream = urlConnection.inputStream

            val br = BufferedReader(InputStreamReader(iStream!!))

            val sb = StringBuffer()

            var line = ""
//            while ((line = br.readLine()) != null) {
//                sb.append(line)
//            }
            while(line!=null){
                line = br.readLine()
                sb.append(line)
            }

            data = sb.toString()
            Log.d("downloadUrl", data.toString())
            br.close()

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))


        mMap!!.setOnMapClickListener { point ->
            // Already two locations
            if (MarkerPoints.size > 1) {
                MarkerPoints.clear()
                mMap!!.clear()
            }

            // Adding new item to the ArrayList
            MarkerPoints.add(point)

            // Creating MarkerOptions
            val options = MarkerOptions()

            // Setting the position of the marker
            options.position(point)

            /**
             * For the start location, the color of marker is GREEN and
             * for the end location, the color of marker is RED.
             */

            /**
             * For the start location, the color of marker is GREEN and
             * for the end location, the color of marker is RED.
             */
            if (MarkerPoints.size == 1) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            } else if (MarkerPoints.size == 2) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            }


            // Add new marker to the Google Map Android API V2
            mMap!!.addMarker(options)

            // Checks, whether start and end locations are captured
            if (MarkerPoints.size >= 2) {
                val origin = MarkerPoints[0]
                val dest = MarkerPoints[1]

                // Getting URL to the Google Directions API
                val url = getUrl(origin, dest)
                Log.d("onMapClick", url.toString())
                val FetchUrl = FetchUrl()

                // Start downloading json data from Google Directions API
                FetchUrl.execute(url)
                //move map camera
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(origin))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
            }
        }

    }


    private inner class ParserTask : AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {

        // Parsing the data in non-ui thread
        override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>> {

            val jObject: JSONObject

            try {
                jObject = JSONObject(jsonData[0])
                Log.d("ParserTask", jsonData[0])
                val parser = DataParser()
                Log.d("ParserTask", parser.toString())

                // Starts parsing data
                var routes: List<List<HashMap<String, String>>>  = parser.parse(jObject)
                Log.d("ParserTask", "Executing routes")
                Log.d("ParserTask", routes.toString())
                return routes

            } catch (e: Exception) {
                Log.d("ParserTask", e.toString())
                e.printStackTrace()
            }

            val r:List<List<HashMap<String, String>>> = ArrayList<ArrayList<HashMap<String, String>>>()
            return r
        }

        // Executes in UI thread, after the parsing process
        override fun onPostExecute(result: List<List<HashMap<String, String>>>) {
            var points: ArrayList<LatLng>
            var lineOptions: PolylineOptions? = null

            // Traversing through all the routes
            for (i in result.indices) {
                points = ArrayList<LatLng>()
                lineOptions = PolylineOptions()

                // Fetching i-th route
                val path = result[i]

                // Fetching all the points in i-th route
                for (j in path.indices) {
                    val point = path[j]

                    val lat = java.lang.Double.parseDouble(point["lat"])
                    val lng = java.lang.Double.parseDouble(point["lng"])
                    val position = LatLng(lat, lng)

                    points.add(position)
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points)
                lineOptions.width(10f)
                lineOptions.color(Color.RED)

                Log.d("onPostExecute", "onPostExecute lineoptions decoded")

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap!!.addPolyline(lineOptions)
            } else {
                Log.d("onPostExecute", "without Polylines drawn")
            }
        }
    }


}
