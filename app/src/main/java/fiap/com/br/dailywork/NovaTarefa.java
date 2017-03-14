package fiap.com.br.dailywork;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fiap.com.br.dailywork.dao.TarefaDAO;
import fiap.com.br.dailywork.dao.Tipo_TarefaDAO;
import fiap.com.br.dailywork.model.Tarefa;
import fiap.com.br.dailywork.model.Tipo_Tarefa;

public class NovaTarefa extends AppCompatActivity {

    public final static int CODE_NOVA_TAREFA = 666;
    private TextInputLayout tilNomeTarefa;
    private Spinner spTarefa;
    private List<Tipo_Tarefa> tarefas;
    private CalendarView cvData;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_tarefa);
        tilNomeTarefa = (TextInputLayout) findViewById(R.id.tilNomeTarefa);
        spTarefa = (Spinner) findViewById(R.id.spTarefa);
        cvData = (CalendarView) findViewById(R.id.cvData);
        Tipo_TarefaDAO Tipo_TarefaDAO = new Tipo_TarefaDAO(this);
        tarefas = Tipo_TarefaDAO.getAll();
        ArrayAdapter<Tipo_Tarefa> adapter =
                new ArrayAdapter<Tipo_Tarefa>(getApplicationContext(),
                        R.layout.tarefa_spinner_item, tarefas);
        adapter.setDropDownViewResource(R.layout.tarefa_spinner_item);
        spTarefa.setAdapter(adapter);
        cvData.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                data = dayOfMonth + "/" + month + "/" + year;
            }
        });
    }

    public void cadastrar(View v) {
        TarefaDAO tarefaDAO = new TarefaDAO(this);
        Tarefa tarefa = new Tarefa();
        tarefa.setNome(String.valueOf(tilNomeTarefa.getEditText().getText()));
        tarefa.setTipo((Tipo_Tarefa) spTarefa.getSelectedItem());
        tarefa.setData(data);
        tarefaDAO.add(tarefa);
        retornaParaTelaAnterior();
    }

    //retorna para tela de lista de tarefaes
    public void retornaParaTelaAnterior() {
        Intent intentMessage = new Intent();
        setResult(CODE_NOVA_TAREFA, intentMessage);
        finish();
    }
}
