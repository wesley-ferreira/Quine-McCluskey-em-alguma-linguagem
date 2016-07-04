public class Main
{
	public static void main (String args[])
	{
		//int[] minterms = {4, 5, 6, 8, 9, 10, 13}; 
		//int[] dontCare = {0, 7, 15};
		
		int[] minterms = {4,8,10,11,12,15};
		int[] dontCare = {9,14};
		
		int result[][] = quine(minterms, dontCare);
		System.out.println("\nImplicantes primários essenciais");
		print_table(result);
	}
		
	
	/**
	 * Realiza toda a operação necessária para o quine mccluskey, encontrandos os implicantes primos
	 * e os implicantes primos essenciais
	 *  
	 * @param  minterms minetermos iniciais, fornecido pelo usuário
	 * @param  dontCare dontCare's iniciais, fornecido pelo usuário
	 * @return matriz de implicantes primos essenciais 
	 */
	public static int[][] quine(int minterms[], int dontCare[])
	{		
		int allTerms[] = new int[minterms.length + dontCare.length];
		
		for(int i = 0; i < minterms.length; i++)
		{
			allTerms[i] = minterms[i]; 
		}
		
		for(int i = 0; i < dontCare.length; i++)
		{
			allTerms[allTerms.length - i - 1] = dontCare[i]; 
		}
		
		int tabela[][] = create_table(allTerms);
		int tabelaMinterms[][] = create_table(minterms);
		
		int tabelaAux1[][] = create_reduced_matrix(tabela);
		int tabelaAux2[][] = create_reduced_matrix(tabelaAux1);
		
		int notUsedMinterms1[][] = not_used_minterms(tabelaAux1);
		int notUsedMinterms2[][] = not_used_minterms(tabelaAux2);
				
//		System.out.println("\nColuna 1");
//		print_table(tabela);
//		
//		System.out.println("\nColuna 2");
//		print_table(tabelaAux1);
//		
//		System.out.println("\nMini termos não usados coluna 2");
//		print_table(notUsedMinterms1);
//		
//		System.out.println("\nColuna 3");
//		print_table(tabelaAux2);
//		
//		System.out.println("\nMini termos não usados coluna 3");
//		print_table(notUsedMinterms2);
		
		int [][] prime_implicant_table = result_implicant_table(notUsedMinterms1, notUsedMinterms2);
	 	return prime_implicant_chat(prime_implicant_table, tabelaMinterms);
	}
	
	/**
	 * Dado uma matriz de implicantes primários e uma outra matriz dos minitermos iniciais, excluindo os "Don't Care",
	 * retorna os implicantes primários essenciais 
	 *  
	 * @param  not_used_minterms matriz de minetermos que ainda não foram usados
	 * @param  initial_minterms matriz de minetermos de entrada do programa
	 * @return matriz de implicantes primos essenciais 
	 */
	
	//TODO: Descobrir os implicantes primários que faltam para que todos os minetermos sejam cobertos 
	public static int[][] prime_implicant_chat(int not_used_minterms[][], int[][] initial_minterms)
	{		
//		System.out.println("not used minterms");
//		print_table(not_used_minterms);
//		System.out.println("tabela minterms");
//		print_table(initial_minterms);	
			
		int newMatrix[][] = new int [initial_minterms.length][not_used_minterms.length];
		
		//percorre a matriz e coloca tudo como -1
		for(int i = 0; i < newMatrix.length; i++)
		{
			for(int j = 0; j < newMatrix[i].length; j++)
			{
				newMatrix[i][j] = -1;
			}
		}
		
		
		//Compara todos os minitermos iniciais com os minetermos não usados
		int prime_implicant_added = 0; //Serve para mudar o index dos elementos que serão adicionados na matriz "newMatrix"
		for(int i = 0; i < initial_minterms.length; i++) //Percorre todos os minitermos iniciais
		{
			for(int j = 0; j < not_used_minterms.length; j++) //Percorre todos os minitermos não usados
			{				
				for(int k = 0; k < 4; k++) //Percorre e compara bit a bit os elementos das matrizes initial_minterms e not_used_minterms
				{
					//Se k for igual a 3 (ultimo bit) e not_used_minterms[j][k] == 2 (not_used_minterms[j][k] == '-')
					//	ou initial_minterms[i][k] == not_used_minterms[j][k] (o bit em initial_minterms[i][k] é igual ao bit em not_used_minterms[i][k])
					//Entao adiciona o indice do not_used_minterms na matriz "newMatrix"
					
					//k == 3 siguinifica que todos os bit anteriores já foram conferidos e permitem que o minetermo not_used_minterms[j]
					//pode representar o minetermo initial_minterms[i]
					if(k == 3 && ((not_used_minterms[j][k] == 2) || (initial_minterms[i][k] == not_used_minterms[j][k]))) 
					{
						//adiciona indice do not_used_minterms que foi verificado e pode representar initial_minterms[i]
						newMatrix[i][prime_implicant_added] = j;
						//incrementa prime_implicant_added para que na proxima inserção na matriz não ocorra em cima do indice que foi gravado agora
						prime_implicant_added++;
						//para o laço de repetição de "k"
						break;
					}
					//Se not_used_minterms[j][k] == 2 (not_used_minterms[j][k] == '-'), não importa o valor de initial_minterms[i], pois será valido tanto para '0'
					//quando para '1', entao usa-se o  'continue' para prosseguir no laço de 'k'
					if(not_used_minterms[j][k] == 2) continue;
					//Se initial_minterms[i][k] == not_used_minterms[j][k] (0 == 0 ou 1 == 1), entao usa-se o  'continue' para prosseguir no laço de 'k'
					if(initial_minterms[i][k] == not_used_minterms[j][k]) continue;
					//Se nenhuma dessas opções forem satisfeitas para o laço de repetição de 'k', é provavel que nunca chegue aqui, 
					//mas é bom usar por uma questao de segurança
					break;
				}
				
			}
			//quando terminar de comparar initial_minterms[i] com todos os not_used_minterms entao prime_implicant_added = 0
			//isso serve para que os indices que forem adicionados na newMatrix começem a ser adicionados no indice 0
			prime_implicant_added = 0;
		}
		
		//array que guarda o compimento de cada array no array de array's "newMatrix"
		//sevirá para saber qual o minetermo pode ser representado apenas por um implicante primário
		int usedIndex[] = new int[newMatrix.length];
		
		for(int i = 0; i < newMatrix.length; i++)
		{
			int length = 0;			
			for(int j = 0; j < newMatrix[i].length; j++)
			{
				//os elementos que guardam -1 não deverão ser contados e representam o fom do array
				if(newMatrix[i][j] != -1) length++;
				else break;
			}
			usedIndex[i] = length;
		}
		
		System.out.println("\nImprime X's");
		for(int i = 0; i < newMatrix.length; i++)
		{
			for(int j = 0; j < usedIndex[i]; j++)
			{
				System.out.print(newMatrix[i][j] + " ");
			}
			System.out.println();
		}
		
		//se só tiver apenas um (1) implicante primário, passar a ser um implicante primário essencial
		//ou seja, se usedIndex[i] == 1
		int essential_implicant[][] = new int[newMatrix.length][];
		System.out.println();
		
		//conta quantos implicantes essenciais primários foram criados
		int essential_added = 0;
		for(int i = 0; i < usedIndex.length; i++)
		{
			System.out.println(usedIndex[i]);
			if(usedIndex[i] == 1)
			{
				essential_implicant[essential_added] = not_used_minterms[newMatrix[i][0]]; 
				essential_added++;
			}
		}
//		int newMatrixWithoutEssential[][] = new int [usedIndex.length - essential_added][not_used_minterms.length];

		return create_matrix_from_biggest_matrix(essential_implicant, essential_added);
	}
		
	
	/**
	 * Recebe duas matrizes e faz a concatenação delas sem repetir elementos.
	 * Cada minetermo aparecerá apenas no retorno dessa função. 
	 *  
	 * @param  matrix1 primeira matriz
	 * @param  matrix2 primeira matriz
	 * @return matrix1 + matrix2, sem repetir elementos 
	 */
	public static int[][] result_implicant_table(int matrix1[][], int matrix2[][])
	{
		int newMatrix[][] = new int[50][];		
		int itensAdded = 0;
		
		for(int i = 0; i < matrix1.length; i++)
		{
			//se item nao existe na matriz, entao adiciona na matriz
			if(!item_exist_in_matrix(newMatrix, matrix1[i]))
			{
				newMatrix[itensAdded] = matrix1[i];
				itensAdded++;
			}
		}
		
		for(int i = 0; i < matrix2.length; i++)
		{
			if(!item_exist_in_matrix(newMatrix, matrix2[i]))
			{
				newMatrix[itensAdded] = matrix2[i];
				itensAdded++;
			}
		}		
		return create_matrix_from_biggest_matrix(newMatrix, itensAdded);
	}
	
	//FUNÇÃO BOOLEANA QUE RECEBE UMA MATRIZ E UM ARRAY RETORNA VERDADEIRO SE EXISTE ALGUMA LINHA DA MATRIZ IGUAL AO ARRAY.
	//FUNÇÃO GENÉRICA.
	public static boolean item_exist_in_matrix(int matrix[][], int term[])
	{
		for (int i = 0; i < matrix.length; i++)
		{
			if(matrix[i] == null) break;
			
			if(matrix[i][0] == term[0] && matrix[i][1] == term[1] && matrix[i][2] == term[2] && matrix[i][3] == term[3])
			{
				return true;
			}
		}
		return false;
	}
		
	//RETORNA UMA MATRIZ EM QUE SUAS LINHAS SÃO MINTERMOS QUE AINDA NÃO FORAM USADOS (NAO FORAM MARCADOS).
	public static int[][] not_used_minterms(int matrix[][])
	{
		int newMatrix[][] = new int[50][];
		int minTermsNotUsed = 0;
		
		for(int i = 0; i < matrix.length - 1; i++)
		{
			boolean minTermUsed = false;
			for(int j = 0; j < matrix.length; j++)
			{
				if(qnt_of_different_terms(matrix[i], matrix[j]) == 1)
				{
					minTermUsed = true;
					break;
				}
			}
			
			if(minTermUsed) continue;
			newMatrix[minTermsNotUsed] = matrix[i];
			minTermsNotUsed++;
		}
		return create_matrix_from_biggest_matrix(newMatrix, minTermsNotUsed);
	}	
	
	//ORDEM DE USO 6 => RECEBE UMA MATRIZ E O NUMERO DE LINHAS DA MATRIZ QUE SERá CRIADA. FUNÇÃO GENÉRICA.
	public static int[][] create_matrix_from_biggest_matrix(int matrix[][], int realUsedLines)
	{
		int newMatrix[][] = new int[realUsedLines][];
		
		for(int i = 0; i < realUsedLines; i++)
		{
			newMatrix[i] = matrix[i];
		}		
		return newMatrix; //RETONA UMA MATRIZ
	}
	
	//ORDEM DE USO 7 => COMPARA TODAS AS LINHAS DA MATRIZ INICIAL E RETORNA UMA NOVA MATRIZ EM QUE SUAS LINHAS SÃO ARRAYS 
	//EM QUE O COMPUTADOR ENCONTROU APENAS UMA DIFERENCA NAS QUATRO COLUNAS. NESTE ALGORITMO NAO É NECESSARIO SEPARAR OS
	//MINTERMOS EM GRUPOS PORQUE ESSA DIVISÃO SÓ SERVE PARA FACILITAR A VISUALIZAÇÃO DO DIGITOS NO PAPEL E APROXIMAR 
	//AS LINHAS QUE PROVAVELMENTE TERÃO UMA SÓ DIFERENÇA. 
	public static int[][] create_reduced_matrix(int matrix[][])
	{		
		int newMatrix[][] = new int[50][];		
		int mintermsCreated = 0;
		for(int i = 0; i < matrix.length - 1; i++)
		{			
			for(int j = i + 1; j < matrix.length; j++)
			{				
				if(qnt_of_different_terms(matrix[i], matrix[j]) == 1)
				{
					newMatrix[mintermsCreated] = create_minterm_from_terms(matrix[i], matrix[j]);
					mintermsCreated++;
				}
			}
		}
		return create_matrix_from_biggest_matrix(newMatrix, mintermsCreated); //EXEMPLO: SE O PARAMETRO FOSSE A MATRIZ
		// matrix[][] = {{0,0,0,0}, {0,0,0,1}, {1,1,1,1}}, O RETORNO SERIA UMA MATRIZ F[][]={{0,0,0,2}}, QUE SE IMPRESSA, MOSTRARIA 000-
	}
	
	//ORDEM DE USO 5 => RECEBE E COMPARA OS DIGITOS DA MESMA COLUNA DE DOIS ARRAYS (QUE INICIALMENTE SÃO AS LINHAS DA MATRIZ INICIAL)
	//E RETORNA UM ARRAY COM O DIGITO QUE SE DEFERENCIOU SUBSTITUIDO POR 2.
	public static int[] create_minterm_from_terms(int minterms1[], int minterms2[])
	{
		int newTerm[] = new int[minterms1.length];

		for(int i = 0; i < minterms1.length; i++)
		{
			if(minterms1[i] == minterms2[i]) 
			{
				newTerm[i] = minterms1[i];
			}
			else
			{
				newTerm[i] = 2;
			}
		}		
		return newTerm; //EXEMPLO SE ESTA FUNCÃO RECEBESSE A[] = {0,0,0,0} E B[] = {0,1,0,0}, ELA RETORNARIA UM ARRAY C[] = {0,2,0,0}.
	}
	
    //IMPRIME UMA MATRIZ DE FORMA QUE OS ELEMENTOS IGUAIS A 2 SEJAM IMPRESSOS COMO TRAÇO "-" 
	public static void print_table(int table[][])
	{
		for(int j = 0; j < table.length; j++)
		{
			print_minterm(table[j]);
			System.out.println();
		}
	}
	
	//FUNÇÃO USADA MAIS DE UMA VEZ. ELA RECEBE UM ARRAY E SE ALGUM DOS ELEMENTOS FOR O NÚMERO 2
	//É IMPRESSO (APENAS IMPRIME, NAO SUSTITUE) "-" NO LUGAR DELE. 
	public static void print_minterm(int minterm[])
	{
		for(int i = 0; i < minterm.length; i++)
		{
			if(minterm[i] == 2)
			System.out.print("-"); //SE RECEBESSE a[] = {0,2,1,2}, SERIA IMPRESSO 0-1-
			else System.out.print(minterm[i]);
		}
	}
	
	//RECEBE DOIS ARRAYS (QUE SAO DUAS LINHAS DE UMA MATRIZ) COMO PARAMETRO E RETORNA O NUMERO DE TERMOS QUE SAO DIFERENTES
	public static int qnt_of_different_terms(int minterms1[], int minterms2[])
	{		
		int qnt_of_different_terms = 0;		
		for(int i = 0; i < 4 ; i++)
		{
			if(minterms1[i] != minterms2[i])
			{
				qnt_of_different_terms++;
			}
		}		
		return qnt_of_different_terms;// SE OS PARAMETROS FOSSEM A[] = {0, 0, 0, 1} e B[] = {1, 1, 1, 1}, A FUNÇÃO RETORNARIA 3,
		                                           //CASO FOSSEM C[] = {0, 1, 0, 0} e D[] = {0, 1, 0, 1}, A FUNÇÃO RETORNARIA 1. 
	}

	//ORDEM DE USO 1 => RECEBE UM NUMERO INTEIRO/MINTERMO E O CONVERTE EM UM BINARIO DE 4 DIGITOS EM FORMA DE STRING	
	public static String decimal_to_binary_string_4_digits(int decimal_minterm) 
	{
		String strBinaryNumber = Integer.toBinaryString(decimal_minterm); 				
		while (strBinaryNumber.length() < 4)
		{    
        	strBinaryNumber = "0" + strBinaryNumber;        
       	} 
       	return strBinaryNumber;  // EXEMPLO: SE A FUNCAO RECEBEU 4, ELA RETORNARA UMA STRING "0100".
    }

    //ORDEM DE USO 2 => RECEBE UMA STRING CONTENDO OS DIGITOS EM BINARIO (EX:0001 OU 1111) E RETORNA UM ARRAY COM ESTES DIGITOS.
    public static String[] binary_digits_to_array(String strBinaryNumber) 

    {
    	String binary_digits_array[] = strBinaryNumber.split("");
    	return binary_digits_array;  // RETORNA ARRAY DE STRING EX: array[] = {0, 0, 0, 1} ou array[] = {1, 1, 1, 1};
    }

    //ORDEM DE USO 3 => RECEBE UM ARRAY DE STRING CONTENDO OS DIGITOS BINARIOS DE UM NUMERO E RETORNA ESTE ARRAY NO TIPO INTEIRO.
    public static int[] string_to_int_array(String[] binary_digits_array)
    {
    	int int_binary_array[]= new int[binary_digits_array.length];
        for(int i = 0; i < binary_digits_array.length; i++)
        {
            int_binary_array[i] = Integer.parseInt(binary_digits_array[i]);        
        }
        return int_binary_array; // EXEMPLO: RECEBEU string array[] = { 0, 1, 0, 1} e retorna int array[] = { 0, 1, 0, 1}.
    }

    //ORDEM DE USO 4 => RECEBE TODOS OS ARRAYS REFERENTES AOS MINTERMOS (EX: int array[] = { 0, 0, 1, 1}) E COM UM LAÇO OS TRANSFORMA
    // NAS LINHAS DE UMA MATRIZ DE INTEIROS
    public static int[][] create_table(int[] minterms)
    {
		int[][] tabela = new int[minterms.length][3];
    	
    	for(int i = 0; i < minterms.length; i++)
    	{
    		tabela[i] = string_to_int_array(binary_digits_to_array(decimal_to_binary_string_4_digits(minterms[i])));
    	}   
       	return tabela; //RETORNA MATRIZ  EX: int matriz[][] = {{0, 0, 0, 0},
       	// 													   {0, 0, 0, 1}...
       	//                                                     {1, 1, 1, 1}}    
    }
}
