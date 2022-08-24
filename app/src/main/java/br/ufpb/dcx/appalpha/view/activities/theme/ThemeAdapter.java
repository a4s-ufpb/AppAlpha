package br.ufpb.dcx.appalpha.view.activities.theme;

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

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {
        private List<Theme> themes;
        private String  TAG = "ThemeListAdapter";
        private Context activityContext;
        private boolean isDeleteMode;

        public ThemeAdapter(List<Theme> themes, Context context, boolean isDeleteMode) {
                this.themes = themes;
                this.activityContext = context;
                this.isDeleteMode = isDeleteMode;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_theme, parent, false);
                return new ViewHolder(v);
        }

        public int getItemCount(){
                return themes.size();
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
                if (themes.get(holder.getAdapterPosition()).getApiId() != null && this.isDeleteMode) {
                        holder.btnDel.setVisibility(View.VISIBLE);
                }

                holder.themeName.setText(themes.get(holder.getAdapterPosition()).getName());
                ImageLoadUtil.getInstance().loadImage(themes.get(holder.getAdapterPosition()).getImageUrl(), holder.themeImage, activityContext);

                holder.themeImage.setOnClickListener(v -> {
                        v.setClickable(false);
                        ThemeActivity.clickInGoToSelectedTheme(themes.get(holder.getAdapterPosition()));
                });

                holder.btnDel.setOnClickListener(v -> {
                        this.deleteSelectedTheme(holder.getAdapterPosition());
                });

        }

        private void deleteSelectedTheme(int position){
                Log.i(TAG, "Theme " + themes.get(position).getName() + " Clicked to delete!");

                AlertDialog.Builder builder = new AlertDialog.Builder(ThemeActivity.activity);
                builder.setCancelable(true);
                builder.setTitle("Tem certeza que deseja apagar o tema \"" + themes.get(position).getName() + "\"?");
                builder.setMessage("Uma vez apagado, ainda será possível importá-lo novamente.");
                builder.setPositiveButton("Sim, apagar",
                        (dialog, which) -> {
                                ThemeSqlService.getInstance(ThemeActivity.activity).deleteById(themes.get(position).getId());
                                Toast.makeText(ThemeActivity.activity, "O tema \"" + themes.get(position).getName() + "\" foi apagado com sucesso.", Toast.LENGTH_LONG).show();

                                this.themes.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, this.themes.size());
                        });
                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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
