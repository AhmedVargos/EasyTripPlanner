package fallenleafapps.com.tripplanner.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.NoteModel;

/**
 * Created by DR Gamal on 3/25/2018.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {



    private List<NoteModel> notesList;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_note, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NoteModel movie = new NoteModel();
        movie = notesList.get(position);
        holder.noteBody.setText(movie.getBody());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView noteBody;

        public MyViewHolder(View view) {
            super(view);
            noteBody = view.findViewById(R.id.noteBody);
        }

    }

    public NotesAdapter(List<NoteModel> notesList) {
        this.notesList = notesList;
    }

}
