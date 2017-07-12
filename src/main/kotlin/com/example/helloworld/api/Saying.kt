package com.example.helloworld.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.base.MoreObjects

class Saying(@JsonProperty val id: Long = 0, @JsonProperty val content: String = "") {
    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("content", content)
                .toString()
    }
}
