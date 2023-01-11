package br.ufpb.dcx.appalpha.view.activities.theme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import br.ufpb.dcx.appalpha.view.activities.AddThemeManagerActivity;
import br.ufpb.dcx.appalpha.view.activities.CreateThemeActivity;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {
        private List<Theme> themes;
        private String  TAG = "ThemeListAdapter";
        private Context activityContext;
        private boolean isDeleteMode;
        private boolean isEditMode;
        private boolean editAnyTheme = true;

        public ThemeAdapter(List<Theme> themes, Context context, boolean isDeleteMode, boolean isEditMode) {
                this.themes = themes;
                this.activityContext = context;
                this.isDeleteMode = isDeleteMode;
                this.isEditMode = isEditMode;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_theme, parent, false);
                return new ViewHolder(v);
        }

        public int getItemCount(){
                return themes.size();
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
                if ((themes.get(holder.getAdapterPosition()).getDeletavel() || themes.get(holder.getAdapterPosition()).getApiId() != null) && this.isDeleteMode) {
                        holder.btnDel.setVisibility(View.VISIBLE);
                }

                if ((editAnyTheme || themes.get(holder.getAdapterPosition()).getDeletavel()) && this.isEditMode) {
                        holder.btnEdit.setVisibility(View.VISIBLE);
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

                holder.btnEdit.setOnClickListener(v -> {
                        holder.btnEdit.setEnabled(false);
                        this.editSelectedTheme(holder.getAdapterPosition());
                });
        }

        private void deleteSelectedTheme(int position)
        {
                Log.i(TAG, "Theme " + themes.get(position).getName() + " Clicked to delete!");

                AlertDialog.Builder builder = new AlertDialog.Builder(ThemeActivity.activity);
                builder.setCancelable(true);
                builder.setTitle("Tem certeza que deseja apagar o tema \"" + themes.get(position).getName() + "\"?");
                builder.setMessage(themes.get(position).getApiId()!=null?String.format("Uma vez apagado, ainda será possível importá-lo novamente.\nInformando o Id: %d",themes.get(position).getApiId()):"Esta operação não pode ser revertida.");
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

        private void editSelectedTheme(int position)
        {
                Log.i(TAG, "Theme " + themes.get(position).toString() + " Clicked to edit!");
                Intent intent = new Intent(activityContext, CreateThemeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("editMode", true);
                intent.putExtra("themeID", themes.get(position).getId());
                activityContext.startActivity(intent);
        }

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
