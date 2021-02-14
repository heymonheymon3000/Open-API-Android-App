package com.codingwithmitch.openapi.ui.main.blog.viewmodel

import android.net.Uri
import android.os.Parcelable
import com.codingwithmitch.openapi.models.BlogPost
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

fun BlogViewModel.setBlogPost(blogPost: BlogPost){
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields.blogPost = blogPost
    setViewState(update)
}

fun BlogViewModel.setIsAuthorOfBlogPost(isAuthorOfBlogPost: Boolean){
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields.isAuthorOfBlogPost = isAuthorOfBlogPost
    setViewState(update)
}

fun BlogViewModel.setQueryExhausted(isExhausted: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryExhausted = isExhausted
    setViewState(update)
}

fun BlogViewModel.setQueryInProgress(isInProgress: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryInProgress = isInProgress
    setViewState(update)
}

fun BlogViewModel.setQuery(query: String){
    val update = getCurrentViewStateOrNew()
    update.blogFields.searchQuery = query
    setViewState(update)
}

fun BlogViewModel.setBlogListData(blogList: List<BlogPost>){
    val update = getCurrentViewStateOrNew()
    update.blogFields.blogList = blogList
    setViewState(update)
}

// Filter can be "date_updated" or "username"
fun BlogViewModel.setBlogFilter(filter: String?){
    filter?.let{
        val update = getCurrentViewStateOrNew()
        update.blogFields.filter = filter
        setViewState(update)
    }
}

// Order can be "-" or ""
// Note: "-" = DESC, "" = ASC
fun BlogViewModel.setBlogOrder(order: String){
    val update = getCurrentViewStateOrNew()
    update.blogFields.order = order
    setViewState(update)
}

fun BlogViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.blogFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

fun BlogViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.blogFields.layoutManagerState = null
    setViewState(update)
}


fun BlogViewModel.removeDeletedBlogPost(){
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields.blogList.toMutableList()
    for(i in 0 until list.size){
        if(list[i] == getBlogPost()){
            list.remove(getBlogPost())
            break
        }
    }
    setBlogListData(list)
//    setViewState(update)
}

fun BlogViewModel.setUpdatedBlogFields(title: String?, body: String?, uri: Uri?){
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updatedBlogFields
    title?.let{ updatedBlogFields.updatedBlogTitle = it }
    body?.let{ updatedBlogFields.updatedBlogBody = it }
    uri?.let{ updatedBlogFields.updatedImageUri = it }
    update.updatedBlogFields = updatedBlogFields
    setViewState(update)
}

fun BlogViewModel.updateListItem(newBlogPost: BlogPost){
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields.blogList.toMutableList()
    for(i in 0 until list.size){
        if(list[i].pk == newBlogPost.pk){
            list[i] = newBlogPost
            break
        }
    }
    update.blogFields.blogList = list
    setViewState(update)
}


fun BlogViewModel.onBlogPostUpdateSuccess(blogPost: BlogPost){
    setUpdatedBlogFields(
        uri = null,
        title = blogPost.title,
        body = blogPost.body
    ) // update UpdateBlogFragment (not really necessary since navigating back)
    setBlogPost(blogPost) // update ViewBlogFragment
    updateListItem(blogPost) // update BlogFragment
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.setUpdatedUri(uri: Uri){
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updatedBlogFields
    updatedBlogFields.updatedImageUri = uri
    update.updatedBlogFields = updatedBlogFields
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.setUpdatedTitle(title: String){
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updatedBlogFields
    updatedBlogFields.updatedBlogTitle = title
    update.updatedBlogFields = updatedBlogFields
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.updateListItem(){
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields.blogList?.toMutableList()
    if(list != null){
        val newBlogPost = getBlogPost()
        for(i in 0 until list.size){
            if(list[i].pk == newBlogPost.pk){
                list[i] = newBlogPost
                break
            }
        }
        update.blogFields.blogList = list
        setViewState(update)
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.setUpdatedBody(body: String){
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updatedBlogFields
    updatedBlogFields.updatedBlogBody = body
    update.updatedBlogFields = updatedBlogFields
    setViewState(update)
}