package de.bergerapps.owlbot.ui.main

import android.os.Bundle
import android.text.Html
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import de.bergerapps.owlbot.R
import de.bergerapps.owlbot.rest.OwlBotResponse
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

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
            initViews(it)
            showViews()
        })
    }

    private fun initViews(owlBotResponse: OwlBotResponse) {
        // word
        word.text = owlBotResponse.word
        // pronunciation
        pronunciation.text = owlBotResponse.pronunciation

        // definition
        owlBotResponse.definitions.forEach { def ->
            definition.text = def.definition
            // example
            if (def.example != null && def.example.isNotEmpty()) {
                example.text = Html.fromHtml(def.example)
            } else {
                example.text = ""
            }
            // type
            if (def.type != null && def.type.isNotEmpty()) {
                word.text = word.text as String + " (" + def.type + ")"
            }
            // image_url
            image_url.text = def.image_url
            if (def.image_url != null && def.image_url.isNotEmpty()) {
                imageView.imageTintList = null
                Picasso.get().load(def.image_url).into(imageView)
            }
            else{
                imageView.setColorFilter(ContextCompat.getColor(context!!, R.color.colorSecond), android.graphics.PorterDuff.Mode.SRC_IN);
                Picasso.get().load(R.drawable.owlbot).into(imageView)
            }
        }
    }

    private fun showViews() {
        word.visibility = View.VISIBLE
        pronunciation.visibility = View.VISIBLE
        definition.visibility = View.VISIBLE
        example.visibility = View.VISIBLE
        image_url.visibility = View.VISIBLE
        imageView.visibility = View.VISIBLE
    }

    private fun hideViews() {
        word.visibility = View.INVISIBLE
        pronunciation.visibility = View.INVISIBLE
        definition.visibility = View.INVISIBLE
        example.visibility = View.INVISIBLE
        image_url.visibility = View.INVISIBLE
        imageView.visibility = View.INVISIBLE
    }

}
