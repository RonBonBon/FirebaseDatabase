package hackeru.binyamin.firebasedatabase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.rvChat)
    RecyclerView rvChat;
    Unbinder unbinder;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnSend)
    public void onViewClicked() {
        //get reference to a table in the database
        DatabaseReference chatTable = FirebaseDatabase.getInstance().getReference("Fruit")
                .child("Apple").child("Pinky-Lady");

        chatTable.setValue(etMessage.getText().toString());

        etMessage.setText(null);
/*
        //add a new row to the table
        DatabaseReference newRow = chatTable.push();
        //setValue(et.getText().toString())
        newRow.setValue(etMessage.getText().toString());
*/
    }
}
