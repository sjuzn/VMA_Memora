package sk.upjs.druhypokus.settings

import android.annotation.SuppressLint
import android.app.LocaleManager
import android.content.Intent
import android.os.Bundle
import android.os.LocaleList
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.main.BackgroundSoundService
import sk.upjs.druhypokus.main.PrefSingleton


class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val zmenaMena = view.findViewById<View>(R.id.zmenaMena)
        val zmenaJazyka = view.findViewById<View>(R.id.zmenaJazyka)
        val reminderMielstone = view.findViewById<View>(R.id.reminderMielstone)
        val reminderTimeCaps = view.findViewById<View>(R.id.reminderTimeCaps)
        val vypZapHudba = view.findViewById<View>(R.id.vypZapHudba)
        val menoSharedPref = view.findViewById<TextView>(R.id.menoSharedPref)
        val textView5 = view.findViewById<TextView>(R.id.textView5)

        menoSharedPref.text = PrefSingleton.getInstance().getPreferenceString("meno")

        reminderTimeCaps.setOnClickListener{
            Toast.makeText(requireContext(), "Not yet implemented", Toast.LENGTH_SHORT).show()
        }

        zmenaMena.setOnClickListener{
            val txt = EditText(this.context)
            txt.hint = menoSharedPref.text

            AlertDialog.Builder(requireContext())
                .setTitle(R.string.edit)
                .setView(txt)
                .setPositiveButton(R.string.confirm
                ) { _, _ ->
                    val t = "" + txt.text.toString()
                    menoSharedPref.text = t
                    PrefSingleton.getInstance().writePreference("meno", t);
                }
                .setNegativeButton(R.string.no
                ) { _, _ -> }
                .show()
        }

        zmenaJazyka.setOnClickListener {
            val lang = arrayOf("en", "sk", "it")

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Pick a language")
            builder.setItems(lang) { _, which ->
                changeLanguage(lang[which])
            }
            builder.show()
        }

        textView5.text = if(PrefSingleton.getInstance().getPreferenceBoolean("hudba")){
            getString(R.string.turn_on_music)
        }else{
            getString(R.string.turn_off_music)
        }

        vypZapHudba.setOnClickListener {
            //zapnuta
            if(PrefSingleton.getInstance().getPreferenceBoolean("hudba")){
                PrefSingleton.getInstance().writePreference("hudba", false);
                val intent = Intent(requireContext(), BackgroundSoundService::class.java)
                requireContext().stopService(intent)
                textView5.text = getString(R.string.turn_on_music)
            }else{
                //vypnuta
                PrefSingleton.getInstance().writePreference("hudba", true);
                val intent = Intent(requireContext(), BackgroundSoundService::class.java)
                requireContext().startService(intent)
                textView5.text = getString(R.string.turn_off_music)
            }
        }

        reminderMielstone.setOnClickListener{
           // showDialog();
            Toast.makeText(requireContext(), "Not yet implemented", Toast.LENGTH_SHORT).show()
        }
        return view
    }
/*
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun showDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout_reminder_settings, null)
        val toggleButton = dialogView.findViewById<Switch>(R.id.toggleButton)
        val timePicker = dialogView.findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle(getString(R.string.menu_settings))
            .setPositiveButton("OK") { dialog, _ ->
                val isOn = toggleButton.isChecked
                val hour = timePicker.hour
                val minute = timePicker.minute
                // handle the settings values
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        dialogBuilder.create().show()
    }
 */
    private fun changeLanguage(lang : String) {
       requireContext().getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(lang)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}