package com.example.happyplace.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplace.R
import com.example.happyplace.model.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_happy_detail.*
import kotlinx.android.synthetic.main.activity_happy_detail.iv_place_image
import kotlinx.android.synthetic.main.activity_happy_detail.tv_description
import kotlinx.android.synthetic.main.item_happy_place.*

class HappyDetailActivity : AppCompatActivity() {

    private var happyDetailModel: HappyPlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_happy_detail)

        happyDetailModel = intent.getParcelableExtra(MainActivity.HAPPY_PLACE_DETAIL)

        happyDetailModel?.let {
            setSupportActionBar(toolbar_detail_place)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = it.title

            toolbar_detail_place.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_place_image.setImageURI(Uri.parse(it.image))
            tv_description.text = it.description
            tv_location.text = it.location

            btn_view_on_map.setOnClickListener{
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra(MainActivity.HAPPY_PLACE_DETAIL, happyDetailModel)
                startActivity(intent)
            }

        }

    }
}