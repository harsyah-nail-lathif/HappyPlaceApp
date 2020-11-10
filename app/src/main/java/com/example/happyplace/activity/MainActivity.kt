package com.example.happyplace.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplace.R
import com.example.happyplace.adapter.HappyPlaceAdapter
import com.example.happyplace.database.DatabaseHandler
import com.example.happyplace.model.HappyPlaceModel
import com.example.happyplace.util.SwipeToDeleteCallBack
import com.example.happyplace.util.SwipeToEditCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        var ADD_HAPPY_ACTIVITY_REQUEST_CODE = 1
        var HAPPY_PLACE_DETAIL = "extra_place_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_add.setOnClickListener {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivityForResult(intent, ADD_HAPPY_ACTIVITY_REQUEST_CODE)
        }
        //untuk get data dari database
        gethappyPlaceFromLocalDB()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_HAPPY_ACTIVITY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                gethappyPlaceFromLocalDB()
            }else{
                Log.e("Activity", "called or back pressed" )
            }
        }
    }

    private fun gethappyPlaceFromLocalDB() {
        //variable supaya data bisa di tampilkan di main activity
        val dbHandler = DatabaseHandler(this)
        //variable untuk menggunakan aksi get di database handler
        val getHappyPlaceList : ArrayList<HappyPlaceModel> = dbHandler.getHappyPlaceList()

        //kondisi ketika data tidak kosong
        if (getHappyPlaceList.size > 0){
            rv_place.visibility = View.VISIBLE
            tv_no_record.visibility = View.GONE
            setupHappyPlaceRV(getHappyPlaceList)
            //kondisi ketika data kosong
        }else{
            rv_place.visibility = View.GONE
            tv_no_record.visibility = View.VISIBLE
        }
    }
    //fungsi untuk menampilkan RecyclerView di main activity
    private fun setupHappyPlaceRV(happyPlaceList: ArrayList<HappyPlaceModel>) {
        //untuk mendeteksi perubahan pada data yang sudah di buat
        rv_place.layoutManager = LinearLayoutManager(this)
        //triger ketika ada data baru
        rv_place.setHasFixedSize(true)

        //untuk menjalankan Adapter di MainActivity agar RecyclerView dapat berjalan
        val adapter = HappyPlaceAdapter(this, happyPlaceList)
        rv_place.adapter = adapter

        adapter.setOnClickListener(object : HappyPlaceAdapter.OnClickedListener{
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, HappyDetailActivity::class.java)
                intent.putExtra(HAPPY_PLACE_DETAIL, model)
                startActivity(intent)
            }
        })

        val editSwipeHandle = object : SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, ADD_HAPPY_ACTIVITY_REQUEST_CODE)
            }
        }
        val editItemTouch = ItemTouchHelper(editSwipeHandle)
        editItemTouch.attachToRecyclerView(rv_place)

        val deleteSwipe = object : SwipeToDeleteCallBack(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_place.adapter as HappyPlaceAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                gethappyPlaceFromLocalDB()
            }
        }

        val deleteitem = ItemTouchHelper(deleteSwipe)
        deleteitem.attachToRecyclerView(rv_place)

    }
}