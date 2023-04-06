package com.example.numbertostringrepresentation.domain.numtotextparser

import java.util.Locale

object NumToTextRepParserFactory {
    fun createParser(locale: Locale): NumToTextRepParser = when (locale) {
        Locale.UK, Locale.US -> EngNumToTextRepParser()
        else -> EngNumToTextRepParser()
    }
}
