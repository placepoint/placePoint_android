package com.phaseII.placepoint.Business.Profile.BusinessDescription

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.phaseII.placepoint.R





class BusinessDescriptionActivity : AppCompatActivity() {
    val MAX_WORDS: Int = 1000
    lateinit  var bus_desc:EditText
    lateinit  var word_count:TextView
    lateinit var toolbar: Toolbar
    lateinit var mTitle: TextView
    lateinit var back: ImageView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_description)
        bus_desc=findViewById(R.id.bus_desc)
        word_count=findViewById(R.id.word_count)
        setToolBar()


        bus_desc.addTextChangedListener(mTextEditorWatcher)
        bus_desc.setText(intent.getStringExtra("description"))

        word_count.text = ""+intent.getStringExtra("description").length+"/1000"
    }

    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title)
        setSupportActionBar(toolbar)


        back = toolbar.findViewById(R.id.back) as ImageView
        back.visibility= View.GONE
        mTitle.text = "Business Description"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_des, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId==android.R.id.home){
            finish()
        }
        if (item.itemId==R.id.done){
            val intent = intent
            intent.putExtra("description", bus_desc.text.toString().trim())
            setResult(453, intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    val mTextEditorWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            word_count.text = s.length.toString() + "/" + MAX_WORDS

        }

        override fun afterTextChanged(s: Editable) {}
    }
}
