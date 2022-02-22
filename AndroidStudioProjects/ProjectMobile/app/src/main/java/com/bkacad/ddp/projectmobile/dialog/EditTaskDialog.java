package com.bkacad.ddp.projectmobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.bkacad.ddp.projectmobile.R;
import com.bkacad.ddp.projectmobile.model.Task;

public abstract class EditTaskDialog extends Dialog {
    public EditTaskDialog(@NonNull Context context) {
        super(context);
    }

    public abstract void sendEditDataFromDialog(Task task);
    private EditText edName;
    private Button btnSave, btnCancel;

    private void initUI(){
        edName = findViewById(R.id.edit_dialog);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_task);
        setCancelable(false);
        initUI();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                if(name.isEmpty()){
                    edName.setError("Hãy nhập task!!");
                    return;
                }

                Task task = new Task();
                task.setName(name);
                sendEditDataFromDialog(task);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
