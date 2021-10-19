package com.example.badmintonapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.badmintonapp.data.Game
import com.example.badmintonapp.data.NODE_GAMES
import com.google.firebase.database.*
import java.lang.Exception

class GamesViewModel : ViewModel() {

    private val dbGames = FirebaseDatabase.getInstance().getReference(NODE_GAMES)

    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>>
        get() = _games

    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game>
        get() = _game

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    fun addGame(game: Game) {
        game.id = dbGames.push().key

        dbGames.child(game.id!!).setValue(game)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val game = snapshot.getValue(Game::class.java)
            game?.id = snapshot.key
            _game.value = game

        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onChildRemoved(snapshot: DataSnapshot) {

        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onCancelled(error: DatabaseError) {

        }
    }

    fun getRealtimeUpdates() {
        dbGames.addChildEventListener(childEventListener)
    }

    fun fetchGames() {
        dbGames.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val games = mutableListOf<Game>()

                    for (gameSnapshot in snapshot.children) {
                        val game = gameSnapshot.getValue(Game::class.java)
                        game?.id = gameSnapshot.key
                        game?.let { games.add(it) }
                    }

                    _games.value = games
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        dbGames.removeEventListener(childEventListener)
    }

}