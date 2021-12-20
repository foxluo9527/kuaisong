package com.ps.wb.ui.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baidu.mapapi.map.*
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener
import com.baidu.mapapi.map.MyLocationConfiguration
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.ps.wb.R
import com.ps.wb.databinding.FragmentAddressMapBinding
import com.ps.wb.state.AddressMapViewModel
import com.ps.wb.ui.base.BaseFragment


class AddressMapFragment : BaseFragment() {
    private lateinit var addressMapViewModel: AddressMapViewModel
    private var _binding: FragmentAddressMapBinding? = null
    private var mMapView: MapView? = null
    private var mBaiduMap: BaiduMap? = null
    private var bitmap: BitmapDescriptor? = null
    private var name = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addressMapViewModel =
            getFragmentViewModelProvider(this).get(AddressMapViewModel::class.java)
        _binding = FragmentAddressMapBinding.inflate(inflater, container, false)
        _binding?.lifecycleOwner = this
        _binding?.click = ClickProxy()
        _binding?.vm = addressMapViewModel
        _binding?.shareVm = sharedVM
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时必须调用mMapView. onResume ()
        mMapView!!.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.flag)
        mMapView = _binding!!.mapView
        mBaiduMap = mMapView?.map
        mBaiduMap?.isMyLocationEnabled = true
        name = sharedVM.chooseAddressInfo.value?.get("key") as String
        val ll: LatLng = sharedVM.chooseAddressInfo.value?.get("pt") as LatLng
        val locationData = MyLocationData.Builder()
            .latitude(ll.latitude)
            .longitude(ll.longitude)
            .build()
        mBaiduMap?.setMyLocationData(locationData)
        updateFlag(ll)
        val config = MyLocationConfiguration(
            MyLocationConfiguration.LocationMode.FOLLOWING, true, null
        )
        mBaiduMap?.setMyLocationConfiguration(config)
        val mapStatusUpdate = MapStatusUpdateFactory.zoomBy(6f)
        mBaiduMap!!.animateMapStatus(mapStatusUpdate)
        initGeoCoder()
        search(ll)
        mBaiduMap?.setOnMapClickListener(object : OnMapClickListener {
            /**
             * 单击地图
             */
            override fun onMapClick(point: LatLng) {

            }

            /**
             * 单击地图中的POI点
             */
            override fun onMapPoiClick(poi: MapPoi) {
                _binding?.tvKey?.text = poi.name
                name = poi.name
                updateFlag(poi.position)
                search(poi.position)
            }
        })
    }

    /**
     * 检索经纬度所在地址
     * @param latLng 经纬度信息
     */
    private fun search(latLng: LatLng) {
        geoCoder!!.reverseGeoCode(ReverseGeoCodeOption().location(latLng).pageNum(0).pageSize(100))
    }

    private var geoCoder: GeoCoder? = null

    private fun initGeoCoder() {
        geoCoder = GeoCoder.newInstance()
        geoCoder?.setOnGetGeoCodeResultListener(object : OnGetGeoCoderResultListener {
            override fun onGetGeoCodeResult(geoCodeResult: GeoCodeResult) {}

            @SuppressLint("SetTextI18n")
            override fun onGetReverseGeoCodeResult(reverseGeoCodeResult: ReverseGeoCodeResult) {
                if (reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    return
                }
                val addressDetail = reverseGeoCodeResult.addressDetail
                //需要的地址信息就在AddressComponent 里
                _binding?.tvDis?.text =
                    addressDetail.district + addressDetail.street + addressDetail.streetNumber
                _binding?.tvKey?.text = name
            }
        })
    }

    private fun updateFlag(ll: LatLng) {
        if (bitmap == null)
            return
        val ooA = MarkerOptions().position(ll).icon(bitmap)
        mBaiduMap!!.clear()
        mBaiduMap!!.addOverlay(ooA)
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时必须调用mMapView. onPause ()
        mMapView!!.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // 清除所有图层
        bitmap?.recycle()
        mBaiduMap!!.clear()
        //在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView!!.onDestroy()
    }

    inner class ClickProxy {
        fun onBackPress() {
            nav().navigateUp()
        }

        fun onSurePress() {
            sharedVM.chooseAddressInfo.value?.put("addressName",
                (_binding?.tvDis?.text.toString()+_binding?.tvKey?.text.toString())
            )
            sharedVM.onChooseMapDown.value =true
            nav().navigateUp()
        }
    }
}