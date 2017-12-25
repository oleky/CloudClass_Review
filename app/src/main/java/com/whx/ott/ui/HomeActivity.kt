package com.whx.ott.ui

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.whx.ott.R
import com.whx.ott.bean.CoursesBean
import com.whx.ott.bean.Soulplates
import com.whx.ott.extentions.DelegatesExt
import com.whx.ott.extentions.showToast
import com.whx.ott.presenter.BaseInfoPresenter
import com.whx.ott.presenter.SearchPresenter
import com.whx.ott.presenter.viewinface.SearchView
import com.whx.ott.util.Const
import com.whx.ott.util.KeyBoardUtils
import com.whx.ott.util.SharedpreferenceUtil
import com.whx.ott.widget.FilmInfoTest
import com.whx.ott.widget.ImageAdapter
import kotlinx.android.synthetic.main.activity_home.*
import java.io.Serializable

/**
 * Created by oleky on 2017/12/11.
 */
class HomeActivity : Activity(), SearchView {

    private var mAdapter: ImageAdapter? = null
    private var mInfoPresenter: BaseInfoPresenter? = null
    private var mSearchPresenter: SearchPresenter? = null
    private val macAddress: String by DelegatesExt.preference(this, Const.MACADDRESS, "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initGallery()
        initEditText()
        mInfoPresenter = BaseInfoPresenter(this)
        mSearchPresenter = SearchPresenter(this)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mInfoPresenter?.getBaseInfo()
        mInfoPresenter?.getTownBaseInfo()
        btn_exit.setOnClickListener {
            initBack()
        }

    }

    private fun initGallery() {
        val imageList = FilmInfoTest.getfilmInfo()
        mAdapter = ImageAdapter(this, imageList)
        fancyCoverFlow.apply {
            setSpacing(-150)
            adapter = mAdapter
            setSelection(1002)
            setOnItemClickListener { parent, view, position, id ->
                when (imageList.get(position % imageList.size).rs) {
                    R.mipmap.jichu_icon ->{
                        val intent = Intent(this@HomeActivity, HighClassActivity::class.java)
                        startActivity(intent)
                    }
                    R.mipmap.tese_icon ->{
                        val intent = Intent(this@HomeActivity, NewFeatureActivity::class.java)
                        val soulplateList: MutableList<Soulplates> = SharedpreferenceUtil.queryObj2Sp(this@HomeActivity, "soulplatelist") as MutableList<Soulplates>
                        intent.putExtra("soulplate_list", soulplateList as Serializable)
                        startActivity(intent)
                    }
                    R.mipmap.town_jichu ->{
                        val intent = Intent(this@HomeActivity, TownClassActivity::class.java)
                        startActivity(intent)
                    }
                    R.mipmap.me_icon ->{
                        val intent = Intent(this@HomeActivity, MineNewRcActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }

    }

    private fun initEditText() {
        search_gallery.apply {
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            setOnEditorActionListener(mOnEditorActionListener)
            val drawable:Drawable? = resources.getDrawable(R.drawable.search)
            drawable?.setBounds(0, 0, 32, 32)
            setCompoundDrawables(null, null, drawable, null)
            setOnClickListener {
                KeyBoardUtils.openKeyBoard(this, this@HomeActivity)
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    KeyBoardUtils.closeKeyBoard(this, this@HomeActivity)
                }
            }
        }
    }

    private val mOnEditorActionListener = TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val str_search = search_gallery.text.toString()
            if (!TextUtils.isEmpty(str_search)) {
                mSearchPresenter?.searchCourse(search_gallery.text.toString(), macAddress, 0, 200)
            }
            true
        }
        false
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            initBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initBack() {
        AlertDialog.Builder(this)
                .setTitle("是否退出当前学生账号？")
                .setMessage("为了避免您的课时损失，请每次都要记得退出哦")
                .setCancelable(true)
                .setNegativeButton("退出账号") { dialog, which ->
                    dialog.dismiss()
                    SharedpreferenceUtil.saveData(this@HomeActivity, Const.USER_ID, "")
                    finish()
                }
                .setPositiveButton("不退出，继续学习") { dialog, which -> dialog.dismiss() }
                .create()
                .show()
    }



    override fun searchSucc(clist: MutableList<CoursesBean>?) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra("courseList", clist as Serializable)
        startActivity(intent)
    }

    override fun searchFailed(message: String) {
        showToast(message)
    }

    override fun getUrl(url: String?) {

    }



}