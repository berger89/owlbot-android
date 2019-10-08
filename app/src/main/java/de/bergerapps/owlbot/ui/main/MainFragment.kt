package de.bergerapps.owlbot.ui.main

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import de.bergerapps.owlbot.R
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

        editText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    hideViews()
                    viewModel.getDictionary(editText.text.toString())
                    return true
                }
                return false
            }
        })

        viewModel.owlBotLiveData?.observe(this, Observer {
            message.text = it.word
            pronunciation.text = it.pronunciation
            it.definitions.forEach { def ->
                definition.text = def.definition
                type.text = def.type
                example.text = def.example
                image_url.text = def.image_url
                Picasso.get().load(def.image_url).into(imageView)
            }
            showViews()
        })
    }

    private fun showViews() {
        message.visibility = View.VISIBLE
        pronunciation.visibility = View.VISIBLE
        definition.visibility = View.VISIBLE
        type.visibility = View.VISIBLE
        example.visibility = View.VISIBLE
        image_url.visibility = View.VISIBLE
        imageView.visibility = View.VISIBLE
    }

    private fun hideViews() {
        message.visibility = View.INVISIBLE
        pronunciation.visibility = View.INVISIBLE
        definition.visibility = View.INVISIBLE
        type.visibility = View.INVISIBLE
        example.visibility = View.INVISIBLE
        image_url.visibility = View.INVISIBLE
        imageView.visibility = View.INVISIBLE
    }

}
