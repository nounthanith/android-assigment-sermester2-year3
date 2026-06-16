package com.salu.project.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.salu.project.R
import com.salu.project.api.RetrofitClient
import com.salu.project.api.SessionManager
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvCreatedAt: TextView
    private lateinit var btnLogout: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        
        sessionManager = SessionManager(requireContext())
        
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvCreatedAt = view.findViewById(R.id.tvCreatedAt)
        btnLogout = view.findViewById(R.id.btnLogout)

        fetchUserProfile()

        btnLogout.setOnClickListener {
            val logoutDialog = LogoutDialogFragment()
            logoutDialog.show(parentFragmentManager, "LogoutDialog")
        }

        return view
    }

    private fun fetchUserProfile() {
        lifecycleScope.launch {
            try {
                val user = RetrofitClient.getUserApi(requireContext()).getCurrentUser()
                tvName.text = "Name: ${user.name}"
                tvEmail.text = "Email: ${user.email}"
                tvCreatedAt.text = "Joined on: ${user.createdAt ?: "N/A"}"
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
