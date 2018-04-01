package fallenleafapps.com.tripplanner.ui.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private Context context;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_note, parent, false);

        context = parent.getContext();
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NoteModel noteModel = new NoteModel();
        noteModel = notesList.get(position);
        holder.noteBody.setText(noteModel.getBody());

        if(!noteModel.isFinished()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.noteIsDoneImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_close_red_200_24dp,null));
            }else{
                holder.noteIsDoneImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_close_red_200_24dp));

            }
        }else{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.noteIsDoneImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_done_white_24dp,null));
            }else{
                holder.noteIsDoneImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_done_white_24dp));

            }
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView noteBody;
        ImageView noteIsDoneImage;
        public MyViewHolder(View view) {
            super(view);
            noteBody = view.findViewById(R.id.noteBody);
            noteIsDoneImage = view.findViewById(R.id.note_is_finished_image);
        }

    }

    public NotesAdapter(List<NoteModel> notesList) {
        this.notesList = notesList;
    }

}
