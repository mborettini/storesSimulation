## Table of contents
* [Logic applied in application](#Logic-applied-in-application)
* [Technologies](#technologies)
* [Examples](#examples)

## Logic applied in application
Zadaniem kodu jest symulacja działalności sklepów, w której zakładamy stochastyczny charakter czynników zewnętrznych (decyzje klientów), zaś w ramach działalności sklepu podejmowane są decyzje (polityka cenowa) mające zrównoważyć niekorzystne tendencje w zakresie aktualnego stanu magazynowego poszczególnych towarów. Procesy stochastyczne modelowane są przy użyciu metody Monte Carlo (czyli pseudolosowym generowaniu, zgodnie z założonym rozkładem, wielkości charakteryzujących proces). W symulacji bierzemy pod uwagę 1000 klientów i 3 sklepy.

Sklepy:

Każdy sklep ma w ofercie te same towary, numerowane od 1 do 8, jednak sklepy mają różne strategie rynkowe. Wszystkie zakupy towaru prowadzone są na kredyt, którego koszt to zawsze 2% w skali miesiąca. Sklep1 sprowadza duże ilości towaru, więc jest obciążony dużym kosztem kredytu. Sklep3 sprowadza małe ilości towarów, sklep2 średnie. Dostawy towaru następują zawsze na początku miesiąca i ich wysokość jest dla określonego towaru w określonym sklepie zawsze taka sama. Jeżeli towar zamówiony przez klienta jest w magazynie, to zamówienie jest od razu realizowane. Jeżeli nie, to trafia do kolejki i oczekuje na dostawę. Ceny towarów we wszystkich sklepach są takie same, uwzględniając stałą 20% marżę, i wzrastają od 80 dla towaru1 do 2000 dla towaru8. Jeżeli jednak w którymś sklepie zapasy towaru w magazynie przekraczają wielkość jego dostaw, to w następnym miesiącu wprowadza promocję na ten towar i sprzedaje go z marżą 2%. Każdy sklep obciążony jest takim samym, stałym kosztem działalności.

Klienci:

Preferencje klienckie określa wiek, który na początku dla każdego klienta losowany jest z rozkładu Gaussa ze średnią 65 lat:

```java
Random r = new Random();
double myG = r.nextGaussian()*20+65;
```

przy czym odrzucamy wartości mniejsze niż 20 i większe niż 100 lat. Tak wylosowanych klientów dzielimy na 8 równych przedziałów wiekowych: [20,30], …, [90,100]. Klienci każdej kategorii wiekowej kupują tylko jeden rodzaj towaru, a więc np. klienci [20, 30] kupują tylko towar1, [30, 40] towar2, itd. Osoby starsze kupują zatem droższe towary (ale rzadziej i w mniejszej ilości – rozkłady (*) i (**) poniżej).

Moment wykonania zakupu jest dla każdego klienta losowany z rozkładu wykładniczego

𝑓(𝑥)=𝜆𝑒−𝜆𝑥

czyli:

```java
double getExp(Random rand, double lambda) { (*)
return -lambda*Math.log(1-rand.nextDouble());
}
```

gdzie lambda(Exp) rośnie wraz z wiekiem, lambda=20 w grupie [20, 30], …, lambda=60 w grupie [90, 100].

Ilość sztuk kupowanego towaru jest za każdym razem losowana z rozkładu Poissona:

𝑓(𝑘,𝜆)=𝜆𝑘𝑒−𝜆𝑘!, 𝑘=0,1,2,…,𝜆>0

do którego wystarczy ta prosta implementacja:

```java
int getPoisson (Random rand, double lambda) { (**)
double l = Math.exp(-lambda);
double p = 1.0;
int k = 0;
do {
k++;
p *= rand.nextDouble();
} while (p > l);
return k - 1;
}
```

gdzie lambda(Poisson) maleje wraz z wiekiem, lambda=15 w grupie [20, 30], …, lambda=4 w grupie [90, 100]. Jeżeli wylosowana wartość k=0, to losowanie jest powtarzane.

Po każdym zakupie klienci określają poziom zadowolenia z zakupu w skali od 1 (jeżeli oczekiwali na zakup dłużej niż miesiąc) do 5 (jeżeli zakup był zrealizowany na bieżąco).

Na początku symulacji (dzień 0) dla każdego klienta czas pierwszego zakupu losowany jest z rozkładu (*) z parametrem lambda odpowiedniej grupy wiekowej. Np. jeżeli wylosowana wartość to 29, to zakup wykonywany jest w dniu 29. Po dokonaniu zakupu, losowany jest dla tego klienta dzień kolejnego zakupu.

Dla klientów, którzy wykonują zakupy w pierwszym miesiącu symulacji, prawdopodobieństwo wyboru każdego sklepu jest takie samo, więc rozkładają się oni równomiernie (losowo) w trzech sklepach. W kolejnych miesiącach wybór sklepu uzależniony jest od:
- aktualnej średniej oceny sklepu przez klientów
- stopnia zadowolenia z poprzednich zakupów dokonywanych przez klienta w określonych sklepach (np. ocena = 1 oznacza, że kolejny zakup na pewno nie będzie wykonany w tym samym sklepie)
- ceny (promocje cenowe)
- wieku (klienci młodsi w większym stopniu kierują się ocenami sklepów, oraz są bardziej skłonni do zmiany sklepu)

Prawdopodobieństwo wyboru sklepu zgodnie z powyższymi założeniami określa poniższa formuła, która nadaje wagi ocenom w zależności od wieku:
*x(s) = a + b + 3c + i * a + j * b*, gdzie:
- s - id sklepu (od 1 do 3), 
- a - średnia ocena sklepu (od 1 do 5),
- b - ocena sklepu wystawiona przez danego klienta (od 1 do 5), 
- c - promocja cenowa (0/1) pomnożona razy 3, żeby nadać jej większą wagę
- i - współczynnik zależny od wieku (im młodsza osoba tym wyższy, ocena sklepu jest dla takich osób bardziej znacząca), 
- j - współczynnik zalezny od wieku (im młodsza osoba tym niższy, personalne odczucia z zakupu nie są aż tak ważne).
Sklep, dla którego wartość funkcji będzie najwyższa "wygrywa" i zostaje wybrany przez klienta.

Czas trwania symulacji to 12 miesięcy. Symulację każdego sklepu należy wykonywać w osobnym wątku. Synchronizacja wątków powinna następować na koniec każdego miesiąca.

Na koniec każdego miesiąca powinny być raportowane podstawowe dane, tzn.: stan magazynu, dane finansowe, ocena sklepu, ilość obsłużonych klientów.

## Technologies
Project is created with:
* Java version: 11.0.12
* Maven version: 4.0.0
* Decimal4j dependency version: 1.0.3

## Examples
To run the application, start 'StoresSimulationApplication' class.

Results of simulation will be printed out to console. Example output for first quarter:

```
JANUARY
+---------+------------+----------------+-----------------+-----------------+----------------------------------------------------------------------------------------------------------------------------------------------+
| Store   | Evaluation | Customers no.  | Income          | Debt            | Warehouse state                                                                                                                              |
+---------+------------+----------------+-----------------+-----------------+----------------------------------------------------------------------------------------------------------------------------------------------+
| store 1 | 5,0        | 174            | 1 527 815,29    | 161 999,36      | product 1: 487, product 2: 537, product 3: 493, product 4: 421, product 5: 427, product 6: 292, product 7: 138, product 8: 81,               |
| store 2 | 5,0        | 157            | 1 257 792,82    | 141 999,44      | product 1: 440, product 2: 325, product 3: 479, product 4: 419, product 5: 321, product 6: 218, product 7: 128, product 8: 71,               |
| store 3 | 5,0        | 149            | 1 244 399,20    | 81 999,68       | product 1: 234, product 2: 216, product 3: 260, product 4: 283, product 5: 259, product 6: 160, product 7: 80, product 8: 41,                |
+---------+------------+----------------+-----------------+-----------------+----------------------------------------------------------------------------------------------------------------------------------------------+
FEBRUARY
+---------+------------+----------------+-----------------+-----------------+----------------------------------------------------------------------------------------------------------------------------------------------+
| Store   | Evaluation | Customers no.  | Income          | Debt            | Warehouse state                                                                                                                              |
+---------+------------+----------------+-----------------+-----------------+----------------------------------------------------------------------------------------------------------------------------------------------+
| store 1 | 5,0        | 337            | 2 889 630,95    | 361 998,56      | product 1: 656, product 2: 737, product 3: 594, product 4: 544, product 5: 503, product 6: 334, product 7: 183, product 8: 100,              |
| store 2 | 5,0        | 314            | 2 601 279,39    | 285 998,87      | product 1: 675, product 2: 415, product 3: 658, product 4: 387, product 5: 321, product 6: 237, product 7: 160, product 8: 72,               |
| store 3 | 4,9        | 302            | 2 473 915,94    | 163 999,36      | product 1: 285, product 2: 217, product 3: 261, product 4: 282, product 5: 256, product 6: 170, product 7: 80, product 8: 41,                |
+---------+------------+----------------+-----------------+-----------------+----------------------------------------------------------------------------------------------------------------------------------------------+
MARCH
+---------+------------+----------------+-----------------+-----------------+----------------------------------------------------------------------------------------------------------------------------------------------+
| Store   | Evaluation | Customers no.  | Income          | Debt            | Warehouse state                                                                                                                              |
+---------+------------+----------------+-----------------+-----------------+----------------------------------------------------------------------------------------------------------------------------------------------+
| store 1 | 5,0        | 514            | 4 411 971,14    | 599 997,61      | product 1: 825, product 2: 946, product 3: 647, product 4: 717, product 5: 483, product 6: 342, product 7: 196, product 8: 119,              |
| store 2 | 5,0        | 466            | 3 883 738,90    | 481 998,09      | product 1: 897, product 2: 450, product 3: 828, product 4: 381, product 5: 321, product 6: 275, product 7: 174, product 8: 98,               |
| store 3 | 4,9        | 455            | 3 695 808,61    | 243 999,04      | product 1: 330, product 2: 216, product 3: 259, product 4: 282, product 5: 257, product 6: 168, product 7: 80, product 8: 40,                |
+---------+------------+----------------+-----------------+-----------------+----------------------------------------------------------------------------------------------------------------------------------------------+

```
