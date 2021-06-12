package com.nguyen.pawn.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nguyen.pawn.model.Word

class HomeViewModel: ViewModel() {

    private val _dailyWords: MutableState<ArrayList<Word>> = mutableStateOf(arrayListOf())
    private val _savedWords: MutableState<ArrayList<Word>> = mutableStateOf(arrayListOf())

    val dailyWords: State<ArrayList<Word>> = _dailyWords
    val savedWords: State<ArrayList<Word>> = _savedWords

    init {
        _dailyWords.value = arrayListOf(
            Word(
                "12",
                "Pepper1",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/ˈpepə(r)/"
            ),
            Word(
                "13",
                "Pepper2",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/ˈpepə(r)/"
            ),
            Word(
                "14",
                "Pepper3",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/ˈpepə(r)/"
            ),
        )

        _savedWords.value = arrayListOf(
            Word(
                "",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "2",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "3",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "4",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "5",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "6",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "7",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "8",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "9",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "10",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "11",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
        )
    }



}