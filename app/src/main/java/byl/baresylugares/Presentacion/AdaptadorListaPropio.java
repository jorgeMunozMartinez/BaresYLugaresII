package byl.baresylugares.Presentacion;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


import byl.baresylugares.Dominio.Recomendacion;
import byl.baresylugares.R;

public class AdaptadorListaPropio extends RecyclerView.Adapter<AdaptadorListaPropio.AdaptadorViewHolder>{

    private ArrayList<Recomendacion> recomendacions;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onItenClickBorrar(int position);
        void onItemClickGPS(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    public AdaptadorListaPropio(Context context, ArrayList<Recomendacion> recomendacions) {
        this.context=context;
        this.recomendacions = recomendacions;
    }

    @Override
    public AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_propio,viewGroup,false);
        return new AdaptadorViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder( AdaptadorViewHolder adaptadorViewHolder, int i) {
        Recomendacion recomendacion = recomendacions.get(i);
        adaptadorViewHolder.txtNombreTipo.setText(recomendacion.getNombreTajeta());
        adaptadorViewHolder.txtFechaTipo.setText(recomendacion.getFecha());
        adaptadorViewHolder.txtDes.setText(recomendacion.getComentario());
        Picasso.with(context).load(recomendacion.getImagenBit()).into(adaptadorViewHolder.imgTipo);
        switch (recomendacion.getTipo())
        {
            case "bar": //Cargar imagen de contactos tipo "familia"
                adaptadorViewHolder.imgTipoLugar.setImageResource(R.drawable.ic_bar);
                break;
            case "lugar": //Cargar imagen de los contactos tipo "amigos"
                adaptadorViewHolder.imgTipoLugar.setImageResource(R.drawable.ic_lugar);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return recomendacions.size();
    }

    class AdaptadorViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNombreTipo;
        private TextView txtFechaTipo;
        private TextView txtDes;
        private ImageView imgTipo;
        private ImageView imgTipoLugar;
        private Button btnBorrar;
        private Button btnGps;

        public AdaptadorViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            txtFechaTipo= itemView.findViewById(R.id.lblFechaLugar);
            txtNombreTipo=itemView.findViewById(R.id.lblNombreLugar);
            txtDes=itemView.findViewById(R.id.lblDes);
            imgTipo = itemView.findViewById(R.id.imgTipo);
            imgTipoLugar = itemView.findViewById(R.id.imgTipoLugar);
            btnBorrar = itemView.findViewById(R.id.btnDelete);
            btnGps = itemView.findViewById(R.id.btnGPS);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            btnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItenClickBorrar(position);
                        }
                    }
                }
            });
            btnGps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemClickGPS(position);
                        }
                    }
                }
            });
        }
    }
}
