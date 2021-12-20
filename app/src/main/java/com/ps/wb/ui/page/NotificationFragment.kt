package com.ps.wb.ui.page

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ps.wb.R
import com.ps.wb.base.data.AppDatabase
import com.ps.wb.base.data.Message
import com.ps.wb.base.data.MessageDao
import com.ps.wb.base.ui.adapter.SimpleBaseBindingAdapter
import com.ps.wb.databinding.FragmentNotificationsBinding
import com.ps.wb.databinding.ItemMessageBinding
import com.ps.wb.state.NotificationsViewModel
import com.ps.wb.ui.LoginActivity
import com.ps.wb.ui.base.BaseFragment
import com.scrb.baselib.entity.DialogType
import com.scrb.baselib.util.DateFormatUtils
import com.scrb.baselib.view.MyDialog
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.coroutines.*

class NotificationFragment : BaseFragment() {
    private lateinit var dao: MessageDao
    private var adapter: SimpleBaseBindingAdapter<Message?, ItemMessageBinding?>? =
        null
    private lateinit var vm: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dao = AppDatabase.getInstance(context).messageDao()
        vm =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        _binding!!.lifecycleOwner = this
        _binding?.vm = vm
        _binding?.click = ClickProxy()
        _binding?.shareVm = sharedVM
        return binding.root
    }

    @ExperimentalCoroutinesApi
    @SuppressLint("StaticFieldLeak", "SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedVM.mainNavBottomVisible.value = View.VISIBLE
        _binding!!.rvMessage.layoutManager = LinearLayoutManager(context)
        adapter = object : SimpleBaseBindingAdapter<Message?, ItemMessageBinding?>
            (context, R.layout.item_message) {
            @SuppressLint("ClickableViewAccessibility", "InflateParams")
            override fun onSimpleBindItem(
                binding: ItemMessageBinding?,
                item: Message?,
                holder: RecyclerView.ViewHolder?
            ) {
                if (item!!.isPoint)
                    binding!!.point.visibility = View.VISIBLE
                else
                    binding!!.point.visibility = View.GONE
                binding.tvMessage.text = item.message
                binding.tvTitle.text = item.title
                binding.tvTime.text = DateFormatUtils.getFormatTime(item.time)

                if (item.type == Message.TYPE_SYS) {
                    Glide.with(context!!).load(R.drawable.ic_sys_message).into(binding.ivIcon)
                    binding.root.setOnClickListener {
                        if (item.isPoint) {
                            binding.point.visibility = View.GONE
                            item.isPoint = false
                            dao.update(item)
                        }
                        MyDialog(mActivity, item.message, DialogType.ONLY_BTN_DIALOG).show()
                    }
                } else if (item.type == Message.TYPE_ORDER_MESSAGE) {
                    Glide.with(context!!).load(R.drawable.ic_order_message).into(binding.ivIcon)
                    binding.root.setOnClickListener {
                        if (item.isPoint) {
                            binding.point.visibility = View.GONE
                            item.isPoint = false
                            dao.update(item)
                        }
                        sharedVM.showOrder.value =
                            AppDatabase.getInstance(context).orderDao().searchByAId(item.orderId)
                        nav().navigate(R.id.action_navigation_notifications_to_detailFragment)
                        sharedVM.mainNavBottomVisible.value = View.GONE
                        sharedVM.shouldExit.value = false
                    }
                }
                binding.root.setOnLongClickListener {
                    val mPopupWindow = PopupWindow(context)
                    val popLayout =
                        LayoutInflater.from(context).inflate(R.layout.item_del, null, false)
                    mPopupWindow.setTouchInterceptor(View.OnTouchListener { _, event ->
                        if (event.action == MotionEvent.ACTION_OUTSIDE) {
                            mPopupWindow.dismiss()
                            return@OnTouchListener false
                        }
                        false
                    })
                    mPopupWindow.contentView = popLayout
                    mPopupWindow.isFocusable = true
                    mPopupWindow.isOutsideTouchable = true
                    mPopupWindow.setBackgroundDrawable(BitmapDrawable())
                    val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    popLayout.measure(w, h)
                    //获取PopWindow宽和高
                    val mHeight = popLayout.measuredHeight
                    val mWidth = popLayout.measuredWidth
                    mPopupWindow.contentView.setOnClickListener {
                        AlertDialog.Builder(requireContext())
                            .setMessage("确认删除此消息?")
                            .setPositiveButton("确认") { dialog, _ ->
                                if (dao.delete(item) > 0) {
                                    getData()
                                }
                                dialog.dismiss()
                            }
                            .setNegativeButton("取消") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                        mPopupWindow.dismiss()
                    }
                    mPopupWindow.showAsDropDown(it, it.width / 2 - mWidth / 2, -mHeight / 2)
                    true
                }
            }
        }
        _binding!!.rvMessage.adapter = adapter
        getData()
        refresh.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    delay(500)
                }
                getData()
                refresh.isRefreshing = false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        adapter?.list = dao.all
        adapter?.notifyDataSetChanged()
        if (adapter!!.list != null && adapter!!.list!!.size > 0) {
            _binding!!.rvMessage.visibility = View.VISIBLE
            vm.emptyVisible.value = View.GONE
        } else {
            vm.emptyVisible.value = View.VISIBLE
            _binding!!.rvMessage.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isLogin)
            vm.unLoginVisible.value = View.VISIBLE
        else
            vm.unLoginVisible.value = View.GONE
        sharedVM.shouldExit.value = true
    }

    inner class ClickProxy {

        fun login() {
            gotoActivity(LoginActivity::class.java)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}