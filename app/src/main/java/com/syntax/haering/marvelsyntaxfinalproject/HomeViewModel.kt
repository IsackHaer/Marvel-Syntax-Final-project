package com.syntax.haering.marvelsyntaxfinalproject

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.syntax.haering.marvelsyntaxfinalproject.data.Repository
import com.syntax.haering.marvelsyntaxfinalproject.data.model.FirestoreSaveModel
import com.syntax.haering.marvelsyntaxfinalproject.data.remote.MarvelApi
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel : ViewModel() {

    private val TAG = "HomeViewModel"

    enum class APIStatus { LOADING, DONE, ERROR }

    val repository = Repository(MarvelApi)
    private val db = FirebaseFirestore.getInstance()
    val authentification = FirebaseAuth.getInstance()

    private val _currentUser = MutableLiveData<FirebaseUser?>(authentification.currentUser)
    val currentUser: LiveData<FirebaseUser?> get() = _currentUser


    val character = repository.characters
    val advertComics = repository.comicAdverts
    val homeSeriesList = repository.homeSeriesList
    val libraryCharList = repository.libraryCharList
    val librarySeriesList = repository.librarySeriesList

    private var dbCharDocID = MutableLiveData<String>()
    private var dbSerieDocID = MutableLiveData<String>()
    private var isAlreadySaved = MutableLiveData<Boolean>(false)
    private var dbCharacterList = MutableLiveData<MutableList<FirestoreSaveModel>>(mutableListOf())
    private var dbSerieList = MutableLiveData<MutableList<FirestoreSaveModel>>(mutableListOf())

    val singleCharacter = repository.singleCharacter
    val singleSerie = repository.singleSerie
    val singleComic = repository.singleComic

    val searchedCharacter = repository.searchedTermCharacter
    val searchedSerie = repository.searchedTermSerie

    val detailSeriesCollection = repository.detailSeriesCollection
    val detailComicsCollection = repository.detailComicsCollection


    private var _apiStatus = MutableLiveData<APIStatus>(APIStatus.DONE)
    val apiStatus: LiveData<APIStatus> get() = _apiStatus

    private var _loadComicStatus = MutableLiveData(APIStatus.DONE)
    val loadComicStatus: LiveData<APIStatus> get() = _loadComicStatus

    private var _loadSerieStatus = MutableLiveData(APIStatus.DONE)
    val loadSerieStatus: LiveData<APIStatus> get() = _loadSerieStatus

    private var _searchCategoryBtnState = MutableLiveData<Boolean>(true)
    val searchCategoryBtnState: LiveData<Boolean> get() = _searchCategoryBtnState

    private var _isSavedInLibrary = MutableLiveData<Boolean>(false)
    val isSavedInLibrary: LiveData<Boolean> get() = _isSavedInLibrary

    private var _toast = MutableLiveData<String?>()
    val toast: LiveData<String?> get() = _toast

    init {
        loadHomeScreenWithData()
    }


    fun signIn(email: String, password: String) {
        authentification.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _currentUser.value = authentification.currentUser
                } else {
                    _toast.value = it.exception?.message
                    _toast.value = null
                }
            }
    }

    fun register(email: String, password: String) {
        authentification.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { Task ->
                if (Task.isSuccessful) {
                    signIn(email, password)
                } else {
                    _toast.value = Task.exception?.message
                    _toast.value = null
                }
            }
    }

    fun signOut() {
        authentification.signOut()
        _currentUser.value = authentification.currentUser
        libraryCharList.value!!.clear()
        librarySeriesList.value!!.clear()
        _isSavedInLibrary.value = false
        Log.d(TAG, "Cleared libraryCharList & librarySeriesList")
    }

    fun loadHomeScreenWithData() {
        viewModelScope.launch {
            launch {
                _apiStatus.value = APIStatus.LOADING
                repository.loadAdvertComicList()
                _apiStatus.value = APIStatus.DONE
            }
            launch { repository.loadHomeSeriesList() }
            launch { repository.loadCharacters() }
        }
    }

    fun loadSingleCharacter(id: Int) {
        viewModelScope.launch {
            repository.loadSingleCharacter(id)
        }
    }

    fun loadSingleSerie(id: Int) {
        viewModelScope.launch {
            repository.loadSingleSerie(id)
        }
    }

    fun loadSingleComic(id: Int) {
        viewModelScope.launch {
            repository.loadSingleComic(id)
        }
    }

    fun searchedTerm(namesStartWith: String, titleStartsWith: String) {
        viewModelScope.launch {
            try {
                _apiStatus.value = APIStatus.LOADING
                repository.loadSearchedTerm(namesStartWith, titleStartsWith)
                _apiStatus.value = APIStatus.DONE
            } catch (e: Exception) {
                _apiStatus.value = APIStatus.ERROR
                Log.d("MainViewModel", "could not load searchTerm : $e")
            }
        }
    }

    fun changeSearchCategory(isOnChar: Boolean) {
        if (isOnChar)
            _searchCategoryBtnState.value = true
        if (!isOnChar)
            _searchCategoryBtnState.value = false
        _searchCategoryBtnState.value = _searchCategoryBtnState.value
    }

    fun loadSerieCollection(SeriesCollectionURI: String) {
        viewModelScope.launch {
            try {
                _loadSerieStatus.value = APIStatus.LOADING
                repository.loadSeriesCollection(SeriesCollectionURI)
                _loadSerieStatus.value = APIStatus.DONE
            } catch (e: Exception) {
                _loadSerieStatus.value = APIStatus.ERROR
                Log.d("HomeViewModel", "could not load Serie collection: $e")
            }

        }
    }

    fun loadComicCollection(ComicsCollectionURI: String) {
        viewModelScope.launch {
            try {
                _loadComicStatus.value = APIStatus.LOADING
                repository.loadComicsCollection(ComicsCollectionURI)
                _loadComicStatus.value = APIStatus.DONE
            } catch (e: Exception) {
                _loadComicStatus.value = APIStatus.ERROR
                Log.d("HomeViewModel", "could not load Comics collection: $e")
            }

        }
    }

    fun loadLibraryCharList() {
        db.collection("user/${currentUser.value?.uid}/characters")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                dbCharacterList.value = result.toObjects(FirestoreSaveModel::class.java)

                //checks if the on device library character list is empty or not
                if (libraryCharList.value!!.isNotEmpty()) {
                    //compares firebase list with api list on the device
                    for (i in dbCharacterList.value!!) {
                        for (character in libraryCharList.value!!) {
                            if (i.marvelID == character.id.toString()) {
                                isAlreadySaved.value = true
                                break
                            } else {
                                isAlreadySaved.value = false
                            }
                        }
                        if (!isAlreadySaved.value!!) {
                            viewModelScope.launch {
                                repository.loadLibraryCharList(i.marvelID.toInt())
                                Log.d(
                                    TAG,
                                    "fun loadLibraryCharList added missing character :  ${dbCharacterList.value}"
                                )
                            }
                        }
                    }
                }
                //if on device libraryCharList == empty
                else {
                    viewModelScope.launch {
                        for (i in dbCharacterList.value!!) {
                            repository.loadLibraryCharList(i.marvelID.toInt())
                            Log.d(
                                TAG,
                                "fun loadLibraryCharList added All characters :  ${dbCharacterList.value}"
                            )
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed loading characters-collection")
            }
    }

    fun loadLibrarySeriesList() {
        db.collection("user/${currentUser.value?.uid}/series")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                dbSerieList.value = result.toObjects(FirestoreSaveModel::class.java)

                //checks if library Series list is empty or not
                if (librarySeriesList.value!!.isNotEmpty()) {
                    //compares the firebase list and the api list on the device
                    for (i in dbSerieList.value!!) {
                        for (serie in librarySeriesList.value!!) {
                            if (i.marvelID == serie.id.toString()) {
                                isAlreadySaved.value = true
                                break
                            } else {
                                isAlreadySaved.value = false
                            }
                        }

                        if (!isAlreadySaved.value!!) {
                            viewModelScope.launch {
                                repository.loadLibrarySeriesList(i.marvelID.toInt())
                                Log.d(TAG, "fun loadLibrarySerieList added missing serie : ${dbSerieList.value}")
                            }
                        }
                    }

                } else {
                    //if on device librarySerieList == empty
                    viewModelScope.launch {
                        for (i in dbSerieList.value!!) {
                            repository.loadLibrarySeriesList(i.marvelID.toInt())
                            Log.d(TAG, "fun loadLibrarySeriesList added all series : ${dbSerieList.value}")
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed loading serie-collection")
            }
    }


    fun getDbCharDocID(id: String) {
        db.collection("user/${currentUser.value?.uid}/characters")
            .get()
            .addOnSuccessListener { result ->
                val dbCharacterList = result.toObjects(FirestoreSaveModel::class.java)
                for (i in dbCharacterList) {
                    if (i.marvelID == id) {
                        dbCharDocID.value = i.docID
                        Log.d(TAG, i.docID)
                        break
                    }
                }
            }
    }

    fun getDbSerieDocID(id: String) {
        db.collection("user/${currentUser.value?.uid}/series")
            .get()
            .addOnSuccessListener { result ->
                val dbSerieList = result.toObjects(FirestoreSaveModel::class.java)
                for (i in dbSerieList) {
                    if (i.marvelID == id) {
                        dbSerieDocID.value = i.docID
                        Log.d(TAG, i.docID)
                        break
                    }
                }
            }
    }

    fun deleteLibraryChar(id: String) {
        db.document("user/${currentUser.value?.uid}/characters/${dbCharDocID.value}")
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "${dbCharDocID.value} character was successfully deleted")
                _isSavedInLibrary.value = false
                repository.deleteLibraryCharacter(id.toInt())
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to delete character : ${it.message}")
            }
    }

    fun deleteLibrarySerie(id: String) {
        db.document("user/${currentUser.value?.uid}/series/${dbSerieDocID.value}")
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "${dbSerieDocID.value} serie was successfully deleted")
                _isSavedInLibrary.value = false
                repository.deleteLibrarySerie(id.toInt())
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to delete serie : ${it.message}")
            }
    }

    fun addLibraryChar(id: String, timestamp: Date) {
        val addChar = FirestoreSaveModel("", id, timestamp)
        db.collection("user/${currentUser.value?.uid}/characters/")
            .add(addChar)
            .addOnSuccessListener {
                loadLibraryCharList()
                Log.d(TAG, "character was successfully added")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to add character : ${it.message}")
            }
    }

    fun addLibrarySerie(id: String, timestamp: Date) {
        val addSerie = FirestoreSaveModel("", id, timestamp)
        db.collection("user/${currentUser.value?.uid}/series/")
            .add(addSerie)
            .addOnSuccessListener {
                loadLibrarySeriesList()
                Log.d(TAG, "serie was successfully added")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to add serie : ${it.message}")
            }
    }

    fun isCharInLibrary(
        charID: Int,
        libraryList: MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>
    ) {
        for (i in libraryList) {
            if (i.id == charID) {
                _isSavedInLibrary.value = true
                getDbCharDocID(charID.toString())
                break
            } else {
                _isSavedInLibrary.value = false
            }
        }
    }

    fun isSerieInLibrary(
        serieID: Int,
        libraryList: MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>
    ) {
        for (i in libraryList) {
            if (i.id == serieID) {
                _isSavedInLibrary.value = true
                getDbSerieDocID(serieID.toString())
                break
            } else {
                _isSavedInLibrary.value = false
            }
        }
    }

}
