file_name = "Petelka.txt"

# Open the file and process each line
with open(file_name, "r") as file:
  for line in file:
    number = line.strip()
    number = number.split(",")
    # if len(number) == 6:
    #   first_five = number[:5]
    #   last_digit = number[5]
    #   print(f'RULES_MAP_CHOUREG2.put("{first_five}", {last_digit});')
    if len(number) == 10:
      first_five = "".join(number[:9])
      last_digit = number[9]
      print(f'RULES_MAP_PETELKA.put("{first_five}", {last_digit});')