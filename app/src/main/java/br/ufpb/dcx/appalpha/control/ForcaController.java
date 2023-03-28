package br.ufpb.dcx.appalpha.control;

import android.widget.ImageView;

import br.ufpb.dcx.appalpha.R;

/**
 * Class for update image when user fail by putting wrong letter
 */
public class ForcaController {
    private ImageView forca;

    /**
     * Alloc instance and setup local variables
     * @param forca
     */
    public ForcaController(ImageView forca) {
        this.forca = forca;
    }

    public void mudaForca(int erro) {
        switch (erro) {
            case 0:
                forca.setImageResource(R.drawable.erro0);
                break;

            case 1:
                forca.setImageResource(R.drawable.erro1);
                break;

            case 2:
                forca.setImageResource(R.drawable.erro2);
                break;

            case 3:
                forca.setImageResource(R.drawable.erro3);
                break;

            case 4:
                forca.setImageResource(R.drawable.erro4);
                break;

            case 5:
                forca.setImageResource(R.drawable.erro5);
                break;

            case 6:
                forca.setImageResource(R.drawable.erro6);
                break;
        }

    }

}
