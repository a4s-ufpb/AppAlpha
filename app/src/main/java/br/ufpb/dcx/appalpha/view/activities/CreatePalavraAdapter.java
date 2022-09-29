package br.ufpb.dcx.appalpha.view.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.service.ThemeSqlService;
import br.ufpb.dcx.appalpha.control.util.ImageLoadUtil;
import br.ufpb.dcx.appalpha.model.bean.Theme;

public class CreatePalavraAdapter extends RecyclerView.Adapter<CreatePalavraAdapter.ViewHolder> {
    private CreateThemeActivity createTheme;
    private String  TAG = "CreatePalavraAdapter";
    private Context activityContext;
    private boolean isDeleteMode;

    public CreatePalavraAdapter(CreateThemeActivity createTheme, Context context, boolean isDeleteMode) {
        this.createTheme = createTheme;
        this.activityContext = context;
        this.isDeleteMode = isDeleteMode;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_theme, parent, false);
        return new ViewHolder(v);
    }

    public int getItemCount(){
        return createTheme.palavras.size();
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (this.isDeleteMode) {
            holder.btnDel.setVisibility(View.VISIBLE);
        }

        holder.themeName.setText(createTheme.palavras.get(holder.getAdapterPosition()).getWord());
        ImageLoadUtil.getInstance().loadImage(createTheme.palavras.get(holder.getAdapterPosition()).getImageUrl(), holder.themeImage, activityContext);

        holder.themeImage.setOnClickListener(v -> {
            v.setClickable(false);
        });

        holder.btnDel.setOnClickListener(v -> {
            this.deleteSelectedTheme(holder.getAdapterPosition());
        });

    }

    private void deleteSelectedTheme(int position){
        Log.i(TAG, "Theme " + createTheme.palavras.get(position).getWord() + " Clicked to delete!");

        createTheme.palavras.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, createTheme.palavras.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView themeImage;
        TextView themeName;
        ImageButton btnDel;

        private ViewHolder(View itemView) {
            super(itemView);
            themeImage = itemView.findViewById(R.id.img_left);
            themeName = itemView.findViewById(R.id.tv_theme_name_left);
            btnDel = itemView.findViewById(R.id.btnDel);
        }

    }

}
