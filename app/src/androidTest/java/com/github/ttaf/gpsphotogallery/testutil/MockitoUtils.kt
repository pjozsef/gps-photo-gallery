package com.github.ttaf.gpsphotogallery.testutil

import org.mockito.Mockito.`when`

fun <T> whenever(methodcall: T) = `when`(methodcall)