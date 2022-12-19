package io.github.tainafernandes.POCAPI.api.integrations;

import com.google.gson.Gson;
import io.github.tainafernandes.POCAPI.api.DTO.AddressViaCepDTO;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.springframework.stereotype.Component;

@Component
public class viaCep {

    public AddressViaCepDTO getCepDTO(String cep) throws Exception{
        URL url = new URL("https://viacep.com.br/ws/"+cep+"/json/");
        URLConnection connection = url.openConnection(); //abrindo conexão
        InputStream is = connection.getInputStream(); //dados da requisição
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        String auxCep = "";
        StringBuilder jsonCep = new StringBuilder();
        while ((auxCep = br.readLine()) != null){
            jsonCep.append(auxCep); //junto para recuperar dados
        }

        var address = new Gson().fromJson(jsonCep.toString(), AddressViaCepDTO.class);
        return address;
    }
}
