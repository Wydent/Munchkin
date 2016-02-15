package fr.univ_littoral.mpillier.testslideview2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageMilieuCombatFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_milieu_combat_layout, container, false);

        // récupération des paramètres
        Bundle args = getArguments();
        String nomJoueur = args.getString("nomJoueur");
        String levelJoueur = args.getString("levelJoueur");
        String attaqueJoueur = args.getString("attaqueJoueur");
        String nomMonstre = args.getString("nomMonstre");
        String levelMonstre = args.getString("levelMonstre");
        String attaqueMonstre = args.getString("attaqueMonstre");

        TextView tvNomJoueurCombat = (TextView) view.findViewById(R.id.nomJoueurCombat);
        TextView tvLevelJoueur = (TextView) view.findViewById(R.id.levelJoueur);
        TextView tvAttaqueJoueur = (TextView) view.findViewById(R.id.attaqueJoueur);
        TextView tvNomMonstreCombat = (TextView) view.findViewById(R.id.nomMonstreCombat);
        TextView tvLevelMonstre = (TextView) view.findViewById(R.id.levelMonstre);
        TextView tvAttaqueMonstre = (TextView) view.findViewById(R.id.attaqueMonstre);

        tvNomJoueurCombat.setText(nomJoueur);
        tvLevelJoueur.setText("lvl "+levelJoueur);
        tvAttaqueJoueur.setText("force "+attaqueJoueur);
        tvNomMonstreCombat.setText(nomMonstre);
        tvLevelMonstre.setText("lvl "+levelMonstre);
        tvAttaqueMonstre.setText("force "+attaqueMonstre);

        return view;
    }
}
