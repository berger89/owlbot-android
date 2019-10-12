package de.bergerapps.owlbot.ui.main

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import de.bergerapps.owlbot.R
import de.bergerapps.owlbot.rest.OwlBotResponse
import kotlinx.android.synthetic.main.main_fragment.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearSnapHelper


class MainFragment : Fragment() {

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
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        initRecyclerView()

        searchWordEditText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action and KeyEvent.ACTION_DOWN == 0 && keyCode == KeyEvent.KEYCODE_ENTER) {
                    hideViews()
                    viewModel.getDictionary(searchWordEditText.text.toString())
                    return true
                }
                return false
            }
        })

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
