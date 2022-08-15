package smu.app.nunsong_market.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import smu.app.nunsong_market.R
import smu.app.nunsong_market.adapter.ContactAdapter
import smu.app.nunsong_market.databinding.FragmentMessageBinding
import smu.app.nunsong_market.dto.Contact


class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding
    private lateinit var adapter: ContactAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    var contactList = ArrayList<Contact>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // status bar 색변경
        val mWindow = requireActivity().window
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        mWindow.statusBarColor = ContextCompat.getColor(requireActivity(),R.color.white)
        // status bar 글자 색 까맣게 만듬
        mWindow.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        binding = FragmentMessageBinding.inflate(inflater, container, false)

        mAuth = Firebase.auth
        mDbRef = FirebaseDatabase.getInstance().getReference()

        configRecyclerView(container)

        return binding.root
    }

    private fun configRecyclerView(container: ViewGroup?) {
        adapter = ContactAdapter(requireContext(), contactList)
        binding.msgListRcv.layoutManager = LinearLayoutManager(requireContext())
        binding.msgListRcv.adapter = adapter

        //contacts의 객체들을 lastTime을 기준으로 사전순 오름차순으로 정렬되어 불러온다.
        val myContactListQuery = mDbRef.child("users").child(mAuth.currentUser!!.uid).child("contacts").orderByChild("lastTime")
        myContactListQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //바뀔 때마다 불려지기 때문에 비우고 다시채우지 않으면 중복으로 쌓인다.
                contactList.clear()

                // 시간순 오름차순으로 정렬되어있기 때문에 가장 최근 메세지가 최상단으로 오기위해서 reverse
                var sortedSnapshot = snapshot.children.reversed()
                for (postSnapshot in sortedSnapshot) {
                    val curretContact = postSnapshot.getValue(Contact::class.java)
                    contactList.add(curretContact!!)

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(container!!.context, "fail to get", Toast.LENGTH_SHORT).show()
            }

        })

    }

    companion object {
        const val TAG: String = "MessageFragment"

        fun newInstance(): MessageFragment {
            return MessageFragment()
        }
    }
}