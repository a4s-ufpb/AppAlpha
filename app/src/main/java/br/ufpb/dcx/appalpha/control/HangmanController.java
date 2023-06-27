package br.ufpb.dcx.appalpha.control;

import android.widget.ImageView;

import br.ufpb.dcx.appalpha.R;

/**
 * Class for update image when user fail by putting wrong letter
 */
public class HangmanController {
    private ImageView hangmanImagem;

    /**
     * Alloc instance and setup local variables
     *
     * @param hangmanView
     */
    public HangmanController(ImageView hangmanView) {
        this.hangmanImagem = hangmanView;
    }

    public void mudaForca(int erro) {
        switch (erro) {
            case 0:
                hangmanImagem.setImageResource(R.drawable.erro0);
                break;

            case 1:
                hangmanImagem.setImageResource(R.drawable.erro1);
                break;

            case 2:
                hangmanImagem.setImageResource(R.drawable.erro2);
                break;

            case 3:
                hangmanImagem.setImageResource(R.drawable.erro3);
                break;

            case 4:
                hangmanImagem.setImageResource(R.drawable.erro4);
                break;

            case 5:
                hangmanImagem.setImageResource(R.drawable.erro5);
                break;

            case 6:
                hangmanImagem.setImageResource(R.drawable.erro6);
                break;
        }

    }

}
