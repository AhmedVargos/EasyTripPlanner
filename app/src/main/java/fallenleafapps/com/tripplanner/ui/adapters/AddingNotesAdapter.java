package fallenleafapps.com.tripplanner.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.NoteModel;

/**
 * Created by DR Gamal on 3/30/2018.
 */

public class AddingNotesAdapter extends RecyclerView.Adapter<AddingNotesAdapter.MyViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private List<NoteModel> notesList;

    //constructor of main class
    public AddingNotesAdapter(Context context, List<NoteModel> notesList) {

        this.context = context;
        this.notesList = notesList;
        inflater = LayoutInflater.from(context);
    }

    //first overriding method
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.add_notes, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    //2nd overriding method
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.textView.setText(notesList.get(position).getBody());

        // pressing on deleteBtn in the list
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(notesList.get(position));
            }
        });
    }

    private void removeItem(NoteModel noteModel) {
        int currPosition = notesList.indexOf(noteModel);
        notesList.remove(currPosition);
        notifyItemRemoved(currPosition);
    }


    //3rd overriding method
    @Override
    public int getItemCount() {
         return notesList.size();
    }


    //class view holder
    class MyViewHolder extends RecyclerView.ViewHolder{

       TextView textView;
       ImageButton deleteBtn;
       ImageButton editBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.addNoteText);
            deleteBtn = (ImageButton) itemView.findViewById(R.id.deleteImageBtn);
            editBtn = (ImageButton) itemView.findViewById(R.id.editImageBtn);



        }
    }
}
