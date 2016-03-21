package fr.univ_littoral.mpillier.testslideview2;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReponseAideFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReponseAideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReponseAideFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String joueurQuiDemande = null;
    private String joueurCible = null;
    private String tresors = null;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReponseAideFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReponseAideFragment newInstance(String param1, String param2) {
        ReponseAideFragment fragment = new ReponseAideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ReponseAideFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // récupération des paramètres
        Bundle args = getArguments();
        joueurQuiDemande = args.getString("joueurQuiDemande");
        joueurCible = args.getString("joueurCible");
        tresors = args.getString("tresors");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View retour = inflater.inflate(R.layout.fragment_reponse_aide, container, false);

        TextView tv1 = (TextView) retour.findViewById(R.id.reponseJoueur);
        TextView tv2 = (TextView) retour.findViewById(R.id.reponseTresorsDemandes);

        tv1.append(joueurQuiDemande + " requiert votre aide.");
        tv2.append(tresors);

        // Inflate the layout for this fragment
        return retour;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
