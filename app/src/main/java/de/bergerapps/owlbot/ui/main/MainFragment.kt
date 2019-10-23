package de.bergerapps.owlbot.ui.main

import android.app.Activity
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import de.bergerapps.owlbot.R
import de.bergerapps.owlbot.service.model.OwlBotResponse
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment(), View.OnKeyListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var adapter: DictionaryAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // init viewmodel
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.owlBotLiveData.observe(this, Observer {
            if (it == null) {
                notFound.text = getString(R.string.not_found, searchWordEditText.text)
                notFound.visibility = View.VISIBLE
                hideViews()
                return@Observer
            }
            updateRecyclerView(it)
            notFound.visibility = View.GONE
            showViews()
        })

        // init views
        searchWordEditText.setOnKeyListener(this)
        initRecyclerView()
    }

    override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action and KeyEvent.ACTION_DOWN == 0 && keyCode == KeyEvent.KEYCODE_ENTER) {
            hideKeyboard(view!!.windowToken)
            hideViews()
            context.let {
                viewModel.getDictionary(context!!, searchWordEditText.text.toString())
            }
            return true
        }
        return false
    }

    private fun hideKeyboard(windowToken: IBinder) {
        (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            windowToken, 0
        )
    }

    private fun updateRecyclerView(owlBotResponse: OwlBotResponse?) {
        adapter.update(owlBotResponse!!)
        pageIndicator.attachTo(recyclerView)
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = DictionaryAdapter(OwlBotResponse(), context!!)
        recyclerView.adapter = adapter
        val linearSnapHelper = SnapHelperOneByOne()
        linearSnapHelper.attachToRecyclerView(recyclerView)

        pageIndicator.attachTo(recyclerView)
    }

    private fun showViews() {
        recyclerView.visibility = View.VISIBLE
        pageIndicator.visibility = View.VISIBLE
    }

    private fun hideViews() {
        recyclerView.visibility = View.GONE
        pageIndicator.visibility = View.GONE
    }

    inner class SnapHelperOneByOne : LinearSnapHelper() {

        override fun findTargetSnapPosition(
            layoutManager: RecyclerView.LayoutManager?,
            velocityX: Int,
            velocityY: Int
        ): Int {

            if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider) {
                return RecyclerView.NO_POSITION
            }

            val currentView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION

            val currentPosition = layoutManager.getPosition(currentView)

            return if (currentPosition == RecyclerView.NO_POSITION) {
                RecyclerView.NO_POSITION
            } else currentPosition

        }
    }

}
