package com.tubes.emusic.ui.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.tubes.emusic.MainActivity
import com.tubes.emusic.R
import com.tubes.emusic.api.*
import com.tubes.emusic.entity.Thumbnail
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    companion object{
        var arrayAlbumdata :List<AlbumData> = ArrayList<AlbumData>()
        var arrayMusicdata :List<MusicData> = ArrayList<MusicData>()
        var arrayArtistdata : List<Artist> = ArrayList<Artist>()
        var arrayRegularUserdata: List<Regularuser> =ArrayList<Regularuser>()
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_search, container, false)

        var textInputLayout = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.input_text_search)
        textInputLayout.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                if(textInputLayout.getEditText()?.getText() != null){
                    Log.e("Abstract", "Pencarian terjadi :"+ textInputLayout.getEditText()?.getText() )
                    laucherWaiting(textInputLayout.getEditText()?.getText().toString())
                    changeFragmentToResult()
                }

                hideKeyboard()
            }
            true
        }

        return view
    }
    // References https://stackoverflow.com/questions/6672066/fragment-inside-fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val childFragment: Fragment = ListGenreFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.child_fragment_container, childFragment).commit()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun laucherWaiting(search : String){
        GlobalScope.launch{
            val hasilAlbum  = AlbumApi.searchAlbumByName(search)?.data!!
            val hasilMusic =  MusicApi.searchMusicByName(search)?.data!!
            val hasilUser = UserApi.searchUser(search)
            delay(1500)
            if(hasilAlbum != null) {
                arrayAlbumdata = hasilAlbum
            }
            if(hasilMusic != null){
                arrayMusicdata = hasilMusic
            }

            if(hasilUser != null) {
                if(hasilUser.artist != null) {
                    arrayArtistdata = hasilUser.artist
                }
                if(hasilUser.regularuser != null) {
                    arrayRegularUserdata = hasilUser.regularuser
                }
            }
            Log.e("Abstract", "hasil album : " +  arrayAlbumdata)
            Log.e("Abstract", "hasil music : " + arrayMusicdata)
            Log.e("Abstract", "hasil user : " + arrayArtistdata )
        }
    }

    fun changeFragmentToResult(){
        val childFragment: Fragment = ResultSearchFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.child_fragment_container, childFragment).commit()
    }



    //References https://stackoverflow.com/questions/41790357/close-hide-the-android-soft-keyboard-with-kotlin
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}