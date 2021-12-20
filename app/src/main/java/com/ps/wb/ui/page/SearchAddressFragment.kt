package com.ps.wb.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener
import com.baidu.mapapi.search.sug.SuggestionResult
import com.baidu.mapapi.search.sug.SuggestionSearchOption
import com.ps.wb.R
import com.ps.wb.base.ui.adapter.SimpleBaseBindingAdapter
import com.ps.wb.databinding.FragmentSearchAddressBinding
import com.ps.wb.databinding.ItemSearchResultBinding
import com.ps.wb.service.LocationService
import com.ps.wb.state.SearchAddressViewModel
import com.ps.wb.ui.base.BaseFragment
import com.xuexiang.citypicker.CityPicker
import com.xuexiang.citypicker.adapter.OnLocationListener
import com.xuexiang.citypicker.adapter.OnPickListener
import com.xuexiang.citypicker.model.City
import com.xuexiang.citypicker.model.HotCity
import com.xuexiang.citypicker.model.LocateState
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import java.util.*


class SearchAddressFragment : BaseFragment(), OnGetSuggestionResultListener {
    private lateinit var vm: SearchAddressViewModel
    private var _binding: FragmentSearchAddressBinding? = null
    private var adapter: SimpleBaseBindingAdapter<HashMap<String, Any>?, ItemSearchResultBinding?>? =
        null
    val mListener: OnBDLocationListener =
        OnBDLocationListener()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm =
            getFragmentViewModelProvider(this).get(SearchAddressViewModel::class.java)
        _binding = FragmentSearchAddressBinding.inflate(inflater, container, false)
        _binding?.lifecycleOwner = this
        _binding?.vm = vm
        _binding?.shareVm = sharedVM
        _binding?.click = ClickProxy()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (sharedVM.onChooseMapDown.value == true) {
            nav().navigateUp()
            return
        }
        vm.mSuggestionSearch.value?.setOnGetSuggestionResultListener(this)
        vm.searchContent.observe(viewLifecycleOwner) {
            if (TextUtils.isEmpty(sharedVM.cityInfo.value)) {
                if (!TextUtils.isEmpty(vm.searchContent.value))
                    vm.searchContent.value = ""
                showShortToast("请先选择地址")
                return@observe
            }
            search()
        }
        sharedVM.cityInfo.observe(viewLifecycleOwner) {
            if (!TextUtils.isEmpty(it) && !TextUtils.isEmpty(vm.searchContent.value))
                search()
        }
        _binding?.rvResult?.layoutManager = LinearLayoutManager(context)
        adapter = object : SimpleBaseBindingAdapter<HashMap<String, Any>?, ItemSearchResultBinding?>
            (context, R.layout.item_search_result) {
            @SuppressLint("SetTextI18n")
            override fun onSimpleBindItem(
                binding: ItemSearchResultBinding?,
                item: HashMap<String, Any>?,
                holder: RecyclerView.ViewHolder?
            ) {
                binding!!.tvKey.text = item?.get("key") as String
                binding.tvDis.text = item["dis"] as String
                binding.root.setOnClickListener {
                    sharedVM.chooseAddressInfo.value = item
                    nav().navigate(R.id.action_searchAddressFragment_to_addressMapFragment)
                }
            }
        }
        _binding?.rvResult?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ClickProxy {
        fun onBackPress() {
            nav().navigateUp()
        }

        fun onChooseCity() {
            initPermission()
        }
    }

    private fun search() {
        // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
        vm.mSuggestionSearch.value?.requestSuggestion(
            SuggestionSearchOption()
                .citylimit(true)
                .keyword(vm.searchContent.value) // 关键字
                .city(sharedVM.cityInfo.value)// 城市
        )
    }

    @SuppressLint("CommitPrefEdits")
    private fun chooseCity() {

        val mHotCities: ArrayList<HotCity> = ArrayList()
        mHotCities.add(HotCity("北京", "北京", "101010100"))
        mHotCities.add(HotCity("上海", "上海", "101020100"))
        mHotCities.add(HotCity("广州", "广东", "101280101"))
        mHotCities.add(HotCity("深圳", "广东", "101280601"))
        mHotCities.add(HotCity("杭州", "浙江", "101210101"))
        CityPicker.from(activity)
            .setLocatedCity(null)
            .setHotCities(mHotCities)
            .setOnPickListener(object : OnPickListener {

                override
                fun onPick(position: Int, data: City) {
                    val cityName = data.name
                    if (!TextUtils.isEmpty(cityName)) {
                        LocationService.stop(mListener)
                        sharedVM.cityInfo.value = cityName
                        context?.getSharedPreferences("data", Context.MODE_PRIVATE)!!
                            .edit()
                            .putString("defaultCity", cityName)
                            .apply()
                    }
                }

                override
                fun onCancel() {
                    LocationService.stop(mListener)
                }

                override
                fun onLocate(locationListener: OnLocationListener?) {
                    //开始定位
                    mListener.setOnLocationListener(locationListener)
                    LocationService.start(mListener)
                }
            })
            .show()
    }

    private fun initPermission() {
        AndPermission.with(this)
            .runtime()
            .permission(Permission.Group.LOCATION)
            .onGranted {
                chooseCity()
            }
            .onDenied {
                showShortToast("请允许获取定位权限")
                chooseCity()
            }
            .start()
    }

    /**
     * 百度定位
     */
    class OnBDLocationListener : BDAbstractLocationListener() {
        private var mOnLocationListener: OnLocationListener? = null
        fun setOnLocationListener(onLocationListener: OnLocationListener?) {
            mOnLocationListener = onLocationListener
        }

        override fun onReceiveLocation(bdLocation: BDLocation) {
            if (mOnLocationListener != null) {
                mOnLocationListener!!.onLocationChanged(
                    LocationService.onReceiveLocation(bdLocation),
                    LocateState.SUCCESS
                )
                LocationService.get().unregisterListener(this)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onGetSuggestionResult(suggestionResult: SuggestionResult?) {
        if (suggestionResult?.allSuggestions == null) {
            if (vm.searchContent.value!!.isNotEmpty())
                showShortToast("数据获取失败，请稍后重试")
            return
        }

        val suggest: MutableList<HashMap<String, Any>> = ArrayList()
        for (info in suggestionResult.allSuggestions) {
            if (info.getKey() != null && info.getDistrict() != null && info.getCity() != null && info.getPt() != null) {
                val map = HashMap<String, Any>()
                map["key"] = info.key
                map["city"] = info.city
                map["dis"] = info.district
                map["pt"] = info.pt
                map["addressName"] = ""
                suggest.add(map)
            }
        }
        adapter?.list = suggest
        adapter?.notifyDataSetChanged()
    }
}