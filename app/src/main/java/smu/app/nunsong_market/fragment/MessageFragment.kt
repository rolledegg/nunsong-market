package smu.app.nunsong_market.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import smu.app.nunsong_market.R
import smu.app.nunsong_market.adapter.ContactAdapter
import smu.app.nunsong_market.adapter.ProductAdapter
import smu.app.nunsong_market.databinding.FragmentMessageBinding
import smu.app.nunsong_market.model.Contact


class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding
    private lateinit var adapter: ContactAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    var contactList = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(inflater,container,false)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        adapter = ContactAdapter(requireContext(), contactList)

        binding.msgListRcv.layoutManager = LinearLayoutManager(requireContext())
        binding.msgListRcv.adapter = adapter

        mDbRef.child("users").child(mAuth.currentUser!!.uid).child("contacts")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //바뀔 때마다 불려지기 때문에 비우고 다시채우지 않으면 중복으로 쌓인다.
                    contactList.clear()

                    for (postSnapshot in snapshot.children) {
                        val curretContact = postSnapshot.getValue(Contact::class.java)
                        contactList.add(curretContact!!)

                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),"fail to get",Toast.LENGTH_SHORT).show()
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