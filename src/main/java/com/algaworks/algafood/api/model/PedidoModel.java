package com.algaworks.algafood.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.ItemPedido;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.StatusPedido;
import com.algaworks.algafood.domain.model.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoModel {

	    private Long id; 
	    private BigDecimal subtotal;
	    private BigDecimal taxaFrete;
	    private BigDecimal valorTotal;
	    private String status;
	    private OffsetDateTime dataCriacao;
	    private OffsetDateTime dataConfirmacao;
	    private OffsetDateTime dataEntrega;
	    private OffsetDateTime dataCancelamento;
	    private RestauranteModelResumo restaurante;
	    private UsuarioModel cliente;
	    private FormaPagamentoModel formaPagamento;
	    private EnderecoModel enderecoEntrega; 
	    private List<ItemPedidoModel> itens;
	    

}
