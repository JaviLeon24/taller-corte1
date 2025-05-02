package com.alejandro.logica;

import com.alejandro.modelo.Pedido;
import java.util.List;

public interface CocinaLogica {

    public void recibirPedido(Pedido pedido);
    
    public void marcarPedidoComoListo(int numeroMesa);
    
    public void removerPedido(Pedido pedido);
    
    List<Pedido> getPedidosListos();

    List<Pedido> getPedidosEnPreparacion();
}
