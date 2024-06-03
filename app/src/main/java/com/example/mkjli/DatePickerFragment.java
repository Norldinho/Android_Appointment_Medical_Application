package com.example.mkjli;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Utiliser la date actuelle comme date par défaut pour le DatePicker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Créer une nouvelle instance de DatePickerDialog et le retourner
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Traiter la date sélectionnée ici
        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
        EditText editTextDateNaissance = getActivity().findViewById(R.id.editTextDateNaissance);
        editTextDateNaissance.setText(selectedDate);
    }
}


