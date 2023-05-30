package com.example.githubperson.data.remote

import com.google.gson.annotations.SerializedName

data class ResponseGithubPerson(

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean? = null,

	@field:SerializedName("items")
	val items: List<ItemsItem>
)

data class ItemsItem(
	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,
)
