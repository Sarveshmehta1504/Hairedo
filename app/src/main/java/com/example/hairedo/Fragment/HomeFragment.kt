package com.example.hairedo.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.hairedo.DataItem
import com.example.hairedo.DataItemType
import com.example.hairedo.MainAdapter
import com.example.hairedo.R
import com.example.hairedo.R.id.image_slider
import com.example.hairedo.RecyclerItem
import com.example.hairedo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var mList: ArrayList<DataItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.adimage1))
        imageList.add(SlideModel("https://s3-alpha-sig.figma.com/img/92c8/bbca/6fcc9b15010fe52ff8ab344669480a68?Expires=1707091200&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=UbxhJWt6vayLUW0Mlvt4WBTotyxwwQClcRdB-QA0~g10~twxrAT~uwBBWeXqC-Q~YZs6~dudC5i4nhw39sjUoFK3iBTlKIthMSZYWmeKy5rW9v8Ne4iabH7ZeRPQcQFtfE3Yd9rusiM6gUnXH~9kvnS8pJ5N4KZy8vTg~m~JChXk-04HjnuPRJV0Gvz3z8vuUhkGrhXx9K7DKRPhoathed5CYjdsju2cISKmAhy7BTrkhWHh~kvboKr5Oit1TQriagKQiZzMFoIN5jXByQ5f7Uh7rQKCUs9d1lkM0bsGK7d2jpeNjR~okKPHCeqhMhU-VdRL15r8eh7DqacZFO9iOQ__"))
        imageList.add(SlideModel("https://s3-alpha-sig.figma.com/img/9f0d/d605/0cda37c018dbc57c172bdeb5bb0c1a1b?Expires=1707091200&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=AO8j8rgblNLSTItwuNAQlVj5iqi9d8CSD561O6BEH3J1Exv7H3rzxuzhaa7DUTyNCoAlrMFbJVSe3whfXzFvVfSOfvpKv4nzQ2taKZmluAVkW-H1obxKAa0B8BLQ~oWeI-PPnYNGgQeT3jeRbPT-7nCgMdHP1H3UUeyM0yQJ4d3uLXEd9xWU1lxKeW7PgwOu0wqPALJOl8vTvviKltahRDxWgLcdAI6LXiOp0wkbLkoTP~HgmrpASLNuljSAS3os2DlBz8Hz8mfu7YrZctn3ZncD3AGpOtWGh6D9tFRvsPvPR48wyUJEx4NXAVJEsxQ96H3FXKGoyH8Q~sa7J~46Sg__"))
        imageList.add(SlideModel(R.drawable.adimage1))
        imageList.add(SlideModel(R.drawable.adimage5))

        _binding =  FragmentHomeBinding.inflate(inflater, container, false)
        val imageSlider = _binding.imageSlider
        imageSlider.setImageList(imageList, scaleType = ScaleTypes.FIT)
        val recommendedRecycleView = _binding.recommendedRv
        recommendedRecycleView.setHasFixedSize(true)
        recommendedRecycleView.layoutManager = LinearLayoutManager(_binding.root.context)
        mList = ArrayList()
        prepareData()

        val adapter = MainAdapter(mList)
        _binding.recommendedRv.adapter = adapter
        return binding.root
    }

    private fun prepareData() {
        val recommendedList = ArrayList<RecyclerItem>()
        val url2 = "https://s3-alpha-sig.figma.com/img/27bb/12e4/7b739a38fa1301690974af3281b6bff4?Expires=1707091200&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=eKrWJ~kbCG2UBOv9qGU7Vgt9kw0Jg6CXK003dui3tHQ-EtY-u8zaMwGRKHeQAwTsu2DZ1cDbZwJnDUnrD1wmNKuIv~c16cwjU7zfRHxGHbiBaPyZd-h~Sc2fCOYQggCTK56Q4BAfhnOEGrP~VGbBL1oyE-t8KUREd0Y0ggxEM~abJTmYgOU2Wa-qHjqTyPz3772e9y5G~sQwi7txYlibZwq1Up9cAVsWVSFUZd4CpoIsUF0GqzGcMUkm5M4USxz5OH50u4qKd-mU8~3hgDqZ7JPbMDoxrDFSBgJ2~-fwfkDgETUyamjScPF8U4ooz3gK3xnj-YJA1pTSM0w7l8CqYQ__"

        recommendedList.add(RecyclerItem(R.drawable.shop_1,"BarberShop","Men's Salon","Last visited","30- 40 min"))
        recommendedList.add(RecyclerItem(R.drawable.shop_2_img,"Shivshakti Hairart","Men's Salon","","15- 42 min"))
        recommendedList.add(RecyclerItem(R.drawable.shop_3_img,"Harnath Hair Salon","Men's Salon","","15- 20 min"))
        recommendedList.add(RecyclerItem(R.drawable.shop_4_img,"The Jawed Habib Salon","Unisex Salon","","10- 15 min"))
        recommendedList.add(RecyclerItem(R.drawable.shop_5_img,"Blossom Professional","Unisex Salon","","10- 25 min"))


        mList.add(DataItem(DataItemType.RECOMMENDED, recommendedList))

    }
}