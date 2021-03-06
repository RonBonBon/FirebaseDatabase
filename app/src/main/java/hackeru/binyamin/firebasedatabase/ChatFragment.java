package hackeru.binyamin.firebasedatabase;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hackeru.binyamin.firebasedatabase.models.ChatItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    FirebaseUser mUser;
    FirebaseDatabase mDatabase;

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

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();

        //readFromDB();
        //readFromDbOnce();
        //readIncremental();

        rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChat.setAdapter(new ChatAdapter());

        return view;
    }

    private void readFromDB() {
        //Non Relational Database
        //1) get a reference to table (case sensitive)
        DatabaseReference chatRef = mDatabase.getReference("ChatItems");
        //2) add a listener for the data
        //Once -> get all the table
        //each change -> update all the data
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot queryResult) {
                //iter
                for (DataSnapshot row : queryResult.getChildren()) {
                    ChatItem item = row.getValue(ChatItem.class);
                    Toast.makeText(getContext(), item.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readFromDbOnce() {
        //1) ref to the table
        DatabaseReference chatRef = mDatabase.getReference("ChatItems");
        //2) add a listener
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot row : dataSnapshot.getChildren()) {
                    ChatItem chatItem = row.getValue(ChatItem.class);
                    Toast.makeText(getContext(), chatItem.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //2.1) inside the listener -> interate over the data and use getValue()
    }

    private void readIncremental() {
        DatabaseReference chatTable = mDatabase.getReference("ChatItems");
        chatTable.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatItem chatItem = dataSnapshot.getValue(ChatItem.class);
                Toast.makeText(getContext(), chatItem.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnSend)
    public void onViewClicked() {
        String uid = mUser.getUid();
        Uri photoUrl = mUser.getPhotoUrl();
        String userName = mUser.getDisplayName();

        String img = null;
        if (photoUrl != null) {
            img = photoUrl.toString();
        }

        String message = etMessage.getText().toString();

        ChatItem item = new ChatItem(message, uid, img, new Date().toString());

        mDatabase.getReference("ChatItems").push().setValue(item);

        etMessage.setText(null);
    }

    public static class ChatAdapter extends FirebaseRecyclerAdapter<ChatItem, ChatAdapter.ChatViewHolder>{

        public ChatAdapter() {
            super(ChatItem.class, R.layout.chat_item, ChatViewHolder.class, FirebaseDatabase.getInstance().getReference("ChatItems"));
        }

        @Override
        protected void populateViewHolder(ChatViewHolder viewHolder, ChatItem model, int position) {
            viewHolder.tvMessage.setText(model.getMessage());
            //viewHolder.tvMessage.setText(us);
            Picasso.with(viewHolder.tvMessage.getContext()).load(model.getProfileImage()).into(viewHolder.ivProfile);
        }

        //ViewHolder findViewById and hold the Views as fields.
        public static class ChatViewHolder extends RecyclerView.ViewHolder {
            ImageView ivProfile;
            TextView tvMessage;

            public ChatViewHolder(View itemView) {
                super(itemView);
                ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
                tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            }
        }
    }
}
