INSERT INTO meal_plan_type (type)
SELECT 'Breakfast'
WHERE NOT EXISTS (SELECT 1 FROM meal_plan_type WHERE type = 'Breakfast');

INSERT INTO meal_plan_type (type)
SELECT 'Lunch'
WHERE NOT EXISTS (SELECT 1 FROM meal_plan_type WHERE type = 'Lunch');

INSERT INTO meal_plan_type (type)
SELECT 'Dinner'
WHERE NOT EXISTS (SELECT 1 FROM meal_plan_type WHERE type = 'Dinner');

INSERT INTO meal_plan_type (type)
SELECT 'Snack'
WHERE NOT EXISTS (SELECT 1 FROM meal_plan_type WHERE type = 'Snack');
