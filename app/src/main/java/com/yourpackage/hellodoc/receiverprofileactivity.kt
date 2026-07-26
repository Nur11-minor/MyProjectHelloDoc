package com.yourpackage.hellodoc

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import android.animation.ValueAnimator
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.yourpackage.hellodoc.adapters.AppointmentAdapter
import com.yourpackage.hellodoc.adapters.CareAdapter
import com.yourpackage.hellodoc.models.Appointment
import com.yourpackage.hellodoc.models.Care
import de.hdodenhof.circleimageview.CircleImageView

class ReceiverProfileActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var profileImage: CircleImageView
    private lateinit var userName: TextView
    private lateinit var greetingText: TextView
    private lateinit var totalAppointments: TextView
    private lateinit var totalCares: TextView
    private lateinit var totalProviders: TextView
    private lateinit var appointmentListView: ListView
    private lateinit var caresListView: ListView
    private lateinit var viewAllAppointments: TextView
    private lateinit var viewAllCares: TextView
    private lateinit var notificationIcon: View
    private lateinit var editProfileIcon: View

    // UI Elements
    private lateinit var btnBookAppointment: MaterialButton
    private lateinit var btnMedicalRecords: MaterialButton

    // NEW: Logout Button
    private lateinit var btnLogout: MaterialButton

    // SharedPreferences for session management
    private lateinit var sharedPreferences: SharedPreferences

    // UI Cards for animation
    private lateinit var profileCard: View
    private lateinit var healthSummaryCard: View

    // Data
    private var appointments = mutableListOf<Appointment>()
    private var cares = mutableListOf<Care>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receiver_profile_activity)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        initViews()
        setupClickListeners()
        loadUserData()
        loadAppointments()
        loadCares()

        // Start Reveal Animations
        startRevealAnimations()
    }

    private fun initViews() {
        profileImage = findViewById(R.id.profileImage)
        userName = findViewById(R.id.userName)
        greetingText = findViewById(R.id.greetingText)
        totalAppointments = findViewById(R.id.totalAppointments)
        totalCares = findViewById(R.id.totalCares)
        totalProviders = findViewById(R.id.totalProviders)
        appointmentListView = findViewById(R.id.appointmentListView)
        caresListView = findViewById(R.id.caresListView)
        viewAllAppointments = findViewById(R.id.viewAllAppointments)
        viewAllCares = findViewById(R.id.viewAllCares)
        notificationIcon = findViewById(R.id.notificationIcon)
        editProfileIcon = findViewById(R.id.editProfileIcon)
        btnBookAppointment = findViewById(R.id.btnBookAppointment)
        btnMedicalRecords = findViewById(R.id.btnMedicalRecords)

        // Initialize Logout Button
        btnLogout = findViewById(R.id.btnLogout)

        // Initialize Cards
        profileCard = findViewById(R.id.profileCard)
        healthSummaryCard = findViewById(R.id.healthSummaryCard)
    }

    private fun startRevealAnimations() {
        // Apply reveal to profile card
        profileCard.post {
            applyInkBleedReveal(profileCard, 0)
        }

        // Apply reveal to health summary card with slight delay
        healthSummaryCard.post {
            applyInkBleedReveal(healthSummaryCard, 200)
        }

        // Apply reveal to profile image
        profileImage.post {
            applyInkBleedReveal(profileImage, 400)
        }
    }

    private fun applyInkBleedReveal(view: View, startDelay: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (API 33+) supports RuntimeShader (AGSL)
            val shader = RuntimeShader(INK_BLEED_SHADER)
            val renderEffect = RenderEffect.createRuntimeShaderEffect(shader, "iContent")
            view.setRenderEffect(renderEffect)

            val animator = ValueAnimator.ofFloat(0f, 1f)
            animator.duration = 1200
            animator.startDelay = startDelay
            animator.addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                shader.setFloatUniform("iProgress", progress)
                shader.setFloatUniform("iResolution", view.width.toFloat(), view.height.toFloat())
                view.invalidate()
            }
            animator.addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    view.setRenderEffect(null) // Clean up
                }
            })
            animator.start()
        } else {
            // Fallback for older versions: Circular Reveal
            view.visibility = View.INVISIBLE
            view.postDelayed({
                view.visibility = View.VISIBLE
                val cx = view.width / 2
                val cy = view.height / 2
                val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
                val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
                anim.duration = 800
                anim.start()
            }, startDelay)
        }
    }

    private fun setupClickListeners() {
        notificationIcon.setOnClickListener {
            // Navigate to notifications
            Toast.makeText(this, "Opening notifications", Toast.LENGTH_SHORT).show()
        }

        editProfileIcon.setOnClickListener {
            // Navigate to edit profile
            Toast.makeText(this, "Opening edit profile", Toast.LENGTH_SHORT).show()
        }

        viewAllAppointments.setOnClickListener {
            // Navigate to all appointments
            Toast.makeText(this, "View all appointments", Toast.LENGTH_SHORT).show()
        }

        viewAllCares.setOnClickListener {
            // Navigate to all cares
            Toast.makeText(this, "View all cares received", Toast.LENGTH_SHORT).show()
        }

        btnBookAppointment.setOnClickListener {
            Toast.makeText(this, "Opening booking screen", Toast.LENGTH_SHORT).show()
        }

        btnMedicalRecords.setOnClickListener {
            Toast.makeText(this, "Opening medical records", Toast.LENGTH_SHORT).show()
        }

        // NEW: Logout Button Click Listener
        btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Quick links
        setupQuickLinks()
    }

    private fun setupQuickLinks() {
        findViewById<View>(R.id.medicalRecordsLink).setOnClickListener {
            Toast.makeText(this, "Opening Medical Records", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.prescriptionsLink).setOnClickListener {
            Toast.makeText(this, "Opening Prescriptions", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.testResultsLink).setOnClickListener {
            Toast.makeText(this, "Opening Test Results", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.helpSupportLink).setOnClickListener {
            Toast.makeText(this, "Opening Help & Support", Toast.LENGTH_SHORT).show()
        }
    }

    // NEW: Logout Confirmation Dialog
    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes, Logout") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    // NEW: Perform Logout
    private fun performLogout() {
        // Clear user session data
        clearUserSession()

        // Show logout confirmation message
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Navigate to Login Activity
        navigateToLogin()
    }

    // NEW: Clear User Session
    private fun clearUserSession() {
        // Clear SharedPreferences
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // If you're using Firebase Auth, uncomment this line
        // FirebaseAuth.getInstance().signOut()
    }

    // NEW: Navigate to Login
    private fun navigateToLogin() {
        // Create intent to go to Login Activity
        // Update "LoginActivity::class.java" with your actual login activity name
        val intent = Intent(this, LoginActivity::class.java)

        // Clear the back stack and start fresh
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        // Finish current activity
        finish()
    }

    private fun loadUserData() {
        // In real app, load from SharedPreferences or API
        userName.text = "Nur E Alam"
        greetingText.text = getGreeting()
        totalAppointments.text = "12"
        totalCares.text = "8"
        totalProviders.text = "5"
    }

    private fun getGreeting(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Good Morning,"
            hour < 16 -> "Good Afternoon,"
            hour < 20 -> "Good Evening,"
            else -> "Good Night,"
        }
    }

    private fun loadAppointments() {
        // In real app, fetch from API
        appointments = mutableListOf(
            Appointment("Dr. Sarah Johnson", "Cardiologist", "Today, 2:30 PM", "Confirmed"),
            Appointment("Dr. Michael Chen", "Dermatologist", "Tomorrow, 10:00 AM", "Pending"),
            Appointment("Dr. Emily Brown", "Neurologist", "Jan 15, 3:00 PM", "Completed"),
            Appointment("Dr. Robert Wilson", "Orthopedic", "Jan 20, 11:00 AM", "Confirmed")
        )

        val adapter = AppointmentAdapter(this, appointments)
        appointmentListView.adapter = adapter

        // Set list height based on content
        appointmentListView.post {
            setListViewHeight(appointmentListView)
        }
    }

    private fun loadCares() {
        // In real app, fetch from API
        cares = mutableListOf(
            Care("Dr. Sarah Johnson", "Health Checkup", "Jan 10, 2024", "Completed"),
            Care("Dr. Michael Chen", "Skin Treatment", "Dec 28, 2023", "Completed"),
            Care("Dr. Emily Brown", "Neurology Consultation", "Dec 15, 2023", "In Progress")
        )

        val adapter = CareAdapter(cares)
        caresListView.adapter = adapter

        // Set list height based on content
        caresListView.post {
            setListViewHeight(caresListView)
        }
    }

    private fun setListViewHeight(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        if (listAdapter.count == 0) {
            val params = listView.layoutParams
            params.height = 0
            listView.layoutParams = params
            return
        }

        var totalHeight = 0
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)

        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(widthMeasureSpec, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
        listView.layoutParams = params
        listView.requestLayout()
    }

    companion object {
        private const val INK_BLEED_SHADER = """
            uniform float2 iResolution;
            uniform float iProgress;
            uniform shader iContent;

            float hash(float2 p) {
                p = fract(p * float2(123.34, 456.21));
                p += dot(p, p + 45.32);
                return fract(p.x * p.y);
            }

            float noise(float2 p) {
                float2 i = floor(p);
                float2 f = fract(p);
                float a = hash(i);
                float b = hash(i + float2(1.0, 0.0));
                float c = hash(i + float2(0.0, 1.0));
                float d = hash(i + float2(1.0, 1.0));
                float2 u = f * f * (3.0 - 2.0 * f);
                return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
            }

            float fbm(float2 p) {
                float v = 0.0;
                float a = 0.5;
                for (int i = 0; i < 4; i++) {
                    v += a * noise(p);
                    p *= 2.0;
                    a *= 0.5;
                }
                return v;
            }

            half4 main(float2 fragCoord) {
                float2 uv = fragCoord / iResolution.xy;
                float2 p = (uv - 0.5) * 2.0;
                p.x *= iResolution.x / iResolution.y;
                
                float d = length(p);
                float n = fbm(uv * 6.0);
                
                float threshold = iProgress * 2.2 - 0.6;
                float bleed = d - n * 0.5;
                
                if (bleed < threshold) {
                    return iContent.eval(fragCoord);
                } else {
                    return half4(0.0);
                }
            }
        """
    }
}