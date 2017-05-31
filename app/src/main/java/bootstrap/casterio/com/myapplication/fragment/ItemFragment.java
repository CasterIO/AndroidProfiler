package bootstrap.casterio.com.myapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bootstrap.casterio.com.myapplication.R;
import bootstrap.casterio.com.myapplication.api.MemeClient;
import bootstrap.casterio.com.myapplication.api.MemeInterface;
import bootstrap.casterio.com.myapplication.model.Meme;
import bootstrap.casterio.com.myapplication.model.PopularMemes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private MemeInterface apiInterface;

    private MyItemRecyclerViewAdapter adapter;
    private List<Meme> memesList;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        memesList = new ArrayList<>();
        adapter = new MyItemRecyclerViewAdapter(memesList, mListener);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(adapter);
        }

        apiInterface = MemeClient.getClient().create(MemeInterface.class);
        Call<PopularMemes> call = apiInterface.loadMemes();
        call.enqueue(new Callback<PopularMemes>() {
            @Override
            public void onResponse(Call<PopularMemes> call, Response<PopularMemes> response) {
                Log.d("MSW", "Call: " + call.request().toString());
                Log.d("MSW", response.code() + "<--- responseCode");
                Log.d("MSW", "response successful: " + response.isSuccessful());
                PopularMemes popularMemes = response.body();
                memesList = popularMemes.getData().getMemes();
                adapter = new MyItemRecyclerViewAdapter(memesList, mListener);
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    recyclerView = (RecyclerView) view;
                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                    }
                    recyclerView.setAdapter(adapter);
                }
                Log.d("MSW", "popular memes count is: " + popularMemes.getData().getMemes().size());
            }

            @Override
            public void onFailure(Call<PopularMemes> call, Throwable t) {
                Log.d("MSW", "response failure");
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Meme item);
    }
}
