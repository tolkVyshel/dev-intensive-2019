package ru.skillbranch.devintensive.viewmodels

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.data.managers.CacheManager.chats
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository
import ru.skillbranch.devintensive.utils.DataGenerator

class MainViewModel: ViewModel() {

    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()){ chats->
        return@map chats.filter { !it.isArchived }
            .map{it.toChatItem()}
            .sortedBy { it.id.toInt() }
    }



    fun getChatData(): LiveData<List<ChatItem>> {
        return chats


    }

  /* private fun loadChats(): List<ChatItem> {
        val chats = chatRepository.loadChats()
        return chats.map {
            it.toChatItem()
        }.sortedBy { it.id.toInt()}

    }*/

 /*   fun addItems(){
        val newItems = DataGenerator.generateChatsWithOffset(chats.value!!.size,5).map { it.toChatItem() }
        val copy = chats.value!!.toMutableList()
        copy.addAll(newItems)
        chats.value = copy.sortedBy { it.id.toInt() }
    }*/
    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

}
