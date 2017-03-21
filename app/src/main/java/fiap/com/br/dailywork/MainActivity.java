package fiap.com.br.dailywork;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fiap.com.br.dailywork.adapter.ListaAndroidAdaper;
import fiap.com.br.dailywork.dao.TarefaDAO;
import fiap.com.br.dailywork.model.Tarefa;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView rvTarefas;
    private ListaAndroidAdaper adapter;
    private TextView id;
    private String temp_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvTarefas = (RecyclerView) findViewById(R.id.rvTarefas);
        temp_id = "";
        final FloatingActionButton fabDel = (FloatingActionButton) findViewById(R.id.fabDel);
        final FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivityForResult(new Intent(MainActivity.this,
                            NovaTarefa.class),
                    NovaTarefa.CODE_NOVA_TAREFA);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        carregaTarefas();
        ItemClickSupport.addTo(rvTarefas).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                id = (TextView) v.findViewById(R.id.id);
                int i = countItemSelected();
                if(!isItemSelected() && i == 0){
                    v.setSelected(true);
                    temp_id = id.getText().toString();
                    fabDel.setVisibility(View.VISIBLE);
                    fabEdit.setVisibility(View.VISIBLE);
                } else {
                    v.setSelected(false);
                    temp_id = "";
                    fabDel.setVisibility(View.INVISIBLE);
                    fabEdit.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Chamado qndo retornar para essa tela da tela de cadastro de um novo torcedor
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, "Cancelado",
                    Toast.LENGTH_LONG).show();
        } else if (requestCode == NovaTarefa.CODE_NOVA_TAREFA) {
            carregaTarefas();
        }
    }

    private void carregaTarefas() {
        TarefaDAO tarefaDAO = new TarefaDAO(this);
        List<Tarefa> tarefas = tarefaDAO.getAll();
        setUpTarefa(tarefas);
    }

    private void setUpTarefa(List<Tarefa> lista) {
        adapter = new ListaAndroidAdaper(this, lista);
        rvTarefas.setLayoutManager(new LinearLayoutManager(this));
        rvTarefas.setAdapter(adapter);
    }

    private boolean isItemSelected(){
        for (int i = 0; i < rvTarefas.getAdapter().getItemCount(); i++) {
            if(rvTarefas.getChildAt(i).isSelected()){
                return true;
            }
        }
        return false;
    }

    private int countItemSelected(){
        int s = 0;
        for (int i = 0; i < rvTarefas.getAdapter().getItemCount(); i++) {
            if(rvTarefas.getChildAt(i).isSelected()){
                s =+ 1;
            }
        }
        return s;
    }

}
