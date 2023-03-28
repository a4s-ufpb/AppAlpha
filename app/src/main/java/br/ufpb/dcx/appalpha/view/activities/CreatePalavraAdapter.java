package br.ufpb.dcx.appalpha.view.activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.util.ImageLoadUtil;

/**
 * Class of Adapter to build the word list with icons
 */
public class CreatePalavraAdapter extends RecyclerView.Adapter<CreatePalavraAdapter.ViewHolder> {
    private CreateThemeActivity createTheme;
    private String  TAG = "CreatePalavraAdapter";
    private Context activityContext;
    private boolean isDeleteMode;
    private boolean isEditMode;

    /**
     * Alloc instance and setup local variables
     * @param createTheme
     * @param context
     * @param isDeleteMode
     * @param isEditMode
     */
    public CreatePalavraAdapter(CreateThemeActivity createTheme, Context context, boolean isDeleteMode, boolean isEditMode) {
        this.createTheme = createTheme;
        this.activityContext = context;
        this.isDeleteMode = isDeleteMode;
        this.isEditMode = isEditMode;
    }

    /**
     * Allocate the view theme
     * @param parent
     * @param viewType
     * @return
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_theme, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Count list of items
     * @return
     */
    public int getItemCount(){
        return createTheme.tema.getChallenges().size();
    }

    /**
     * Populate the word icon view
     * @param holder
     * @param position
     */
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (this.isDeleteMode) {
            holder.btnDel.setVisibility(View.VISIBLE);
        }
        if(this.isEditMode) {
            holder.btnEdit.setVisibility(View.VISIBLE);
        }

        holder.themeName.setText(createTheme.tema.getChallenges().get(holder.getAdapterPosition()).getWord());
        ImageLoadUtil.getInstance().loadImage(createTheme.tema.getChallenges().get(holder.getAdapterPosition()).getImageUrl(), holder.themeImage, activityContext);

        holder.themeImage.setOnClickListener(v -> {
            v.setClickable(false);
        });

        holder.btnDel.setOnClickListener(v -> {
            this.deleteSelectedTheme(holder.getAdapterPosition());
        });

        holder.btnEdit.setOnClickListener(v -> {
            this.editSelectedTheme(holder.getAdapterPosition());
        });
    }

    /**
     * Delete action button
     * @param position
     */
    private void deleteSelectedTheme(int position)
    {
        Log.i(TAG, "Palavra " + createTheme.tema.getChallenges().get(position).getWord() + " Clicked to delete!");
        createTheme.removePalavra(createTheme.tema.getChallenges().get(position));
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, createTheme.tema.getChallenges().size());
    }

    /**
     * Edit button action
     * @param position
     */
    private void editSelectedTheme(int position)
    {
        Log.i(TAG, "Palavra " + createTheme.tema.getChallenges().get(position).getWord() + " Clicked to edit!");
        createTheme.editPalavra(createTheme.tema.getChallenges().get(position));
    }

    /**
     * Class for word icon view
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView themeImage;
        TextView themeName;
        ImageButton btnDel;
        ImageButton btnEdit;
        private ViewHolder(View itemView) {
            super(itemView);
            themeImage = itemView.findViewById(R.id.img_left);
            themeName = itemView.findViewById(R.id.tv_theme_name_left);
            btnDel = itemView.findViewById(R.id.btnDel);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }

    }

}
