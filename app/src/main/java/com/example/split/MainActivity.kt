package com.example.split

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.split.Databasec.DBRaceLog
import com.example.split.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var sharedPref: SharedPreferences

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    var tag: Tag? = null
    lateinit var writeTagFilters: Array<IntentFilter>

    public lateinit var data: RecyclerView

    private lateinit var dbRaceLog: DBRaceLog

//    private val timeLogViewModel: TimeLogViewModel by viewModels {
//        TimeLogViewModelFactory((application as TimeLogApplication).repo)
//    }

    companion object {
        var writeMode = false
        var writeText = ""
        var activescan = false
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.mainToolbar.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.drawerView
        val navController = findNavController(R.id.navHostFragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.writeNFC, R.id.scanNFC, R.id.profile
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerView : View = navView.getHeaderView(0)
        val nameHeader : TextView = headerView.findViewById(R.id.nameHeader)
        val usernameHeader : TextView = headerView.findViewById(R.id.usernameHeader)

        sharedPref = this.getSharedPreferences("key", Context.MODE_PRIVATE)!!

        nameHeader.setText(sharedPref.getString("name", null).toString())
        usernameHeader.setText(sharedPref.getString("username", null).toString())

        dbRaceLog = DBRaceLog(this)

//        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
//        if (nfcAdapter == null) {
//            Toast.makeText(this, "Device does not support NFC", Toast.LENGTH_LONG).show()
//        }
//
//        pendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
//            0
//        )
//        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
//        tagDetected.addCategory(Intent.CATEGORY_DEFAULT)
//        writeTagFilters = arrayOf(tagDetected)
    }

//    fun processNFC(intent: Intent){
//        if(writeMode == false){
//            if(activescan){
//                readFromIntent(intent)
//                Toast.makeText(this, "Scanned Successfully!", Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(this, "Please turn on Active Scan", Toast.LENGTH_SHORT).show()
//            }
//        }else if(writeMode){
//            if(writeText != ""){
//                try {
//                    tag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?
//                    writeNFC(writeText, tag)
//                    closeWriteDialog()
//                    Toast.makeText(this, "Confirmed!", Toast.LENGTH_SHORT).show()
//
//                }catch (e: Exception){
//                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
//                }
//            }else{
//                Toast.makeText(this, "Cant write NFC! Either Text in empty or press the Confirm button to continue", Toast.LENGTH_LONG).show()
//            }
//        }else{
//            Toast.makeText(this, "Go to Write Or Scan page to get started", Toast.LENGTH_LONG).show()
//        }
//    }
//    private fun readFromIntent(intent: Intent) {
//        tag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?
//        val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
//        val msgs = mutableListOf<NdefMessage>()
//        if (rawMsgs != null) {
//            for (i in rawMsgs.indices) {
//                msgs.add(i, rawMsgs[i] as NdefMessage)
//            }
//            buildTagViews(msgs.toTypedArray())
//        }
//    }
//
//    private fun buildTagViews(msgs: Array<NdefMessage>) {
//        if (msgs == null || msgs.isEmpty()) return
//        var text = ""
//        val payload = msgs[0].records[0].payload
//        val textEncoding: Charset = if ((payload[0] and 128.toByte()).toInt() == 0) Charsets.UTF_8 else Charsets.UTF_16
//        val languageCodeLength: Int = (payload[0] and 51).toInt()
//        val time = DateTimeFormatter.ofPattern("HH:mm:ss")
//        try {
//            text = String(
//                payload,
//                languageCodeLength + 1,
//                payload.size - languageCodeLength - 1,
//                textEncoding
//            )
//            dbRaceLog.insertLog(text, LocalDateTime.now().format(time))
//        } catch (e: UnsupportedEncodingException) {
//            Log.e("UnsupportedEncoding", e.toString())
//        }
//    }
//    @Throws(IOException::class, FormatException::class)
//    fun writeNFC(text: String, tag: Tag?) {
//        val records = arrayOf(createNFCRecord(text))
//        val message = NdefMessage(records)
//        // Get an instance of Ndef for the tag.
//        val ndef = Ndef.get(tag)
//        // Enable I/O
//        ndef.connect()
//        // Write the message
//        ndef.writeNdefMessage(message)
//        // Close the connection
//        ndef.close()
//    }
//    @Throws(UnsupportedEncodingException::class)
//    fun createNFCRecord(text: String): NdefRecord {
//        val lang = "en"
//        val textBytes = text.toByteArray()
//        val langBytes = lang.toByteArray(charset("US-ASCII"))
//        val langLength = langBytes.size
//        val textLength = textBytes.size
//        val payload = ByteArray(1 + langLength + textLength)
//
//        // set status byte (see NDEF spec for actual bits)
//        payload[0] = langLength.toByte()
//
//        // copy langbytes and textbytes into payload
//        System.arraycopy(langBytes, 0, payload, 1, langLength)
//        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength)
//        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), payload)
//    }
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        processNFC(intent)
//        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
//            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
//        }
//    }
//
//    public override fun onPause() {
//        super.onPause()
//        nfcAdapter.disableForegroundDispatch(this)
//    }
//
//    public override fun onResume() {
//        super.onResume()
//        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        sharedPref = this.getSharedPreferences("key", Context.MODE_PRIVATE)!!
        val editor: SharedPreferences.Editor = sharedPref.edit()
        val intent = Intent(this, MainActivity::class.java)
        when(item.itemId){
            R.id.logoutbtn -> {
                editor.clear()
                editor.apply()
                finish()
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setMode(mode: Boolean, message: String){
        writeMode = mode
        writeText = message
    }

    fun setActiveScan(mode: Boolean){
        activescan = mode
    }

    fun closeWriteDialog(){
        val writeDialog = supportFragmentManager.findFragmentByTag("write dialog")
        if (writeDialog != null) {
            val df = writeDialog as ShowWriteDialog
            df.dismiss()
        }
    }

}