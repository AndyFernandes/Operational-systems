#!/bin/bash
# Agenda

function buscar {
	while read f
		do
			echo $f[0]
		  case $f[0] in
				$1)	echo $f	;;
		  esac
		done < agenda.csv
}

function adicionar {
	echo $1,$2 >> agenda.csv 
}

# function editar {

# }

# function remover {

# }

echo "1. Buscar contato"
echo "2. Adicionar contato"
echo "3. Editar contato"
echo "4. Remover contato"
echo "5. Sair"
read opcao
	case $opcao in
		1)
			echo "Escreva o nome"
			read nome
			buscar ${nome}
			;;
		2)
			echo "Escreva o nome"
			read nome
			echo "Escreva o número"
			read numero
			adicionar ${nome} ${numero}
			;;
		# 3)
		# 	echo "Escreva o nome"
		# 	read nome
		# 	editar nome
		# 		# funcao buscar
		# 	;;
		# 4)
		# 	echo "Escreva o nome"
		# 	read nome
		# 	remover nome
				# funcao buscar
			# ;;
		*)
			echo "Sorry, I don't understand"
			
			;;
	esac