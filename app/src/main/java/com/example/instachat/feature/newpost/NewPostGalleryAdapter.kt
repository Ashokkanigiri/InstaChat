/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.example.instachat.feature.newpost

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instachat.R
import com.example.instachat.databinding.LayoutItemImageBinding
import com.google.gson.Gson


class NewPostGalleryAdapter(val viewModel: NewPostViewModel
) : ListAdapter<Image, NewPostGalleryAdapter.GalleryViewHolder>(
    DiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = DataBindingUtil.inflate<LayoutItemImageBinding>(inflater, R.layout.layout_item_image, parent, false)
    return GalleryViewHolder(binding)
  }

  override fun getItemId(position: Int): Long {
    return getItem(position).id
  }

  override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  private class DiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
      return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
      return oldItem == newItem
    }
  }

  inner class GalleryViewHolder(val binding: LayoutItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(image: Image){
      binding.image = image
      var isClicked = false

      binding.ivImage.setOnClickListener {
        isClicked = !isClicked
        if(isClicked){
          viewModel.selectedAndCapturedList.add(image.uri.toString())
          binding.ivImage.foreground = ColorDrawable(Color.parseColor("#90D0D0D0"))
        }else{
          viewModel.selectedAndCapturedList.remove(image.uri.toString())
          binding.ivImage.foreground = null
        }
      }
    }
  }
}